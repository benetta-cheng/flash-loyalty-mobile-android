package com.ibm2105.loyaltyapp;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ibm2105.loyaltyapp.database.Account;
import com.ibm2105.loyaltyapp.database.Cart;
import com.ibm2105.loyaltyapp.database.CartItem;
import com.ibm2105.loyaltyapp.database.CartItemDao;
import com.ibm2105.loyaltyapp.database.Item;
import com.ibm2105.loyaltyapp.database.LoyaltyDatabase;

import java.util.ArrayList;
import java.util.List;

public class PreOrderViewModel extends AndroidViewModel {

    private final MutableLiveData<List<PreOrderListData>> items;
    private final MutableLiveData<List<PreOrderListData>> confirmationItems;

    public PreOrderViewModel(@NonNull Application application) {
        super(application);
        LoyaltyDatabase database = LoyaltyDatabase.getDatabase(getApplication());
        LiveData<List<Item>> preprocessedItemsLiveData = database.itemDao().getAllItems();
        List<PreOrderListData> listOfItems = new ArrayList<>();
        items = new MutableLiveData<>(new ArrayList<>());
        confirmationItems = new MutableLiveData<>(new ArrayList<>());

        preprocessedItemsLiveData.observeForever(new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> preprocessedItems) {
                for (Item item : preprocessedItems) {
                    listOfItems.add(new PreOrderListData(item.getItemId(), item.getImage(), 0, item.getPrice(), item.getName()));
                }

                items.setValue(listOfItems);

                // Remove the observer right after getting a value since we will only be observing once
                preprocessedItemsLiveData.removeObserver(this);
            }
        });

        // Hardcoded for now. TODO: Get user from SharedPreferences
        LiveData<Cart> cartLiveData = database.cartDao().getCartForUser(1);
        cartLiveData.observeForever(new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                if (cart != null) {
                    LiveData<List<CartItem>> cartItemLiveData = database.cartItemDao().getAllCartItemForCart(cart.getId());
                    cartItemLiveData.observeForever(new Observer<List<CartItem>>() {
                        @Override
                        public void onChanged(List<CartItem> cartItems) {
                            for (CartItem cartItem : cartItems) {
                                for (PreOrderListData preOrderListData : listOfItems) {
                                    if (preOrderListData.getId() == cartItem.getItemId()) {
                                        preOrderListData.setItemQuantity(cartItem.getQuantity());
                                    }
                                }
                            }
                            items.setValue(listOfItems);

                            // Remove the observer right after getting a value since we will only be observing once
                            cartItemLiveData.removeObserver(this);
                        }
                    });
                }

                // Remove the observer right after getting a value since we will only be observing once
                cartLiveData.removeObserver(this);
            }
        });

    }

    public LiveData<List<PreOrderListData>> getItems() {
        return items;
    }

    public LiveData<List<PreOrderListData>> getConfirmationItems() {
        return confirmationItems;
    }

    public LiveData<Cart> getCart() {
        LoyaltyDatabase database = LoyaltyDatabase.getDatabase(getApplication());
        int userId = PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(R.string.user_id), -1);
        return database.cartDao().getCartForUser(userId);
    }

    public LiveData<List<CartItem>> getCartItems(int cartId) {
        LoyaltyDatabase database = LoyaltyDatabase.getDatabase(getApplication());
        CartItemDao cartItemDao = database.cartItemDao();
        System.out.println("Testing");
        return cartItemDao.getAllCartItemForCart(cartId);
    }

    public void makeConfirmationPreOrderListData(List<CartItem> cartItems) {
        List<PreOrderListData> listOfItems = new ArrayList<PreOrderListData>();
        for (CartItem cartItem : cartItems) {
            LoyaltyDatabase database = LoyaltyDatabase.getDatabase(getApplication());
            LiveData<List<Item>> preprocessedItemsLiveData = database.itemDao().getAllItems();

            preprocessedItemsLiveData.observeForever(new Observer<List<Item>>() {
                @Override
                public void onChanged(List<Item> preprocessedItems) {
                    for (Item item : preprocessedItems) {
                        if (item.getItemId() == cartItem.getItemId()) {
                            listOfItems.add(new PreOrderListData(item.getItemId(), item.getImage(), cartItem.getQuantity(), item.getPrice(), item.getName()));
                        }
                    }

                    // Remove the observer right after getting a value since we will only be observing once
                    preprocessedItemsLiveData.removeObserver(this);
                }
            });
        }
        System.out.println("Size: " + listOfItems.size());
        confirmationItems.setValue(listOfItems);
    }

    public void initCartItems(List<PreOrderListData> listOfItems, String date) {
        LoyaltyDatabase database = LoyaltyDatabase.getDatabase(getApplication());
        int userId = PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(R.string.user_id), -1);
        LiveData<Cart> cartLiveData = database.cartDao().getCartForUser(userId);
        cartLiveData.observeForever(new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                if (cart != null) {
                    LiveData<List<CartItem>> cartItemLiveData = database.cartItemDao().getAllCartItemForCart(cart.getId());
                    cartItemLiveData.observeForever(new Observer<List<CartItem>>() {
                        @Override
                        public void onChanged(List<CartItem> cartItems) {
                            for (CartItem cartItem : cartItems) {
                                for (PreOrderListData preOrderListData : listOfItems) {
                                    if (preOrderListData.getId() == cartItem.getItemId()) {
                                        if (preOrderListData.getItemQuantity() == 0) {
                                            LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                                                database.cartItemDao().deleteCartItem(cartItem);
                                            });
                                        } else {
                                            cartItem.setQuantity(preOrderListData.getItemQuantity());
                                            LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                                                database.cartItemDao().updateCartItem(cartItem);
                                            });
                                        }
                                    } else if (preOrderListData.getItemQuantity() > 0) {
                                        CartItem newCartItem = new CartItem(cart.getId(), preOrderListData.getId(), preOrderListData.getItemQuantity());
                                        LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                                            database.cartItemDao().insert(newCartItem);
                                        });
                                    }
                                }
                            }
                            items.setValue(listOfItems);

                            // Remove the observer right after getting a value since we will only be observing once
                            cartItemLiveData.removeObserver(this);
                        }
                    });
                } else {
                    Cart newCart = new Cart(userId, date);
                    LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                        database.cartDao().insert(newCart);
                    });
                    cartLiveData.observeForever(new Observer<Cart>() {
                        @Override
                        public void onChanged(Cart cart) {
                            if (cart != null) {
                                for (PreOrderListData preOrderListData : listOfItems) {
                                    if (preOrderListData.getItemQuantity() > 0) {
                                        CartItem newCartItem = new CartItem(cart.getId(), preOrderListData.getId(), preOrderListData.getItemQuantity());
                                        LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                                            database.cartItemDao().insert(newCartItem);
                                        });
                                    }
                                }

                                // Remove the observer right after getting a value since we will only be observing once
                                cartLiveData.removeObserver(this);
                            }
                        }
                    });

                }

                // Remove the observer right after getting a value since we will only be observing once
                cartLiveData.removeObserver(this);
            }
        });
    }
}
