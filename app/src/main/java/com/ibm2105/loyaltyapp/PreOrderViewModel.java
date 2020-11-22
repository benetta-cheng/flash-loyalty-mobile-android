package com.ibm2105.loyaltyapp;

import android.app.Application;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ibm2105.loyaltyapp.database.Cart;
import com.ibm2105.loyaltyapp.database.CartDao;
import com.ibm2105.loyaltyapp.database.CartItem;
import com.ibm2105.loyaltyapp.database.CartItemDao;
import com.ibm2105.loyaltyapp.database.Item;
import com.ibm2105.loyaltyapp.database.LoyaltyDatabase;
import com.ibm2105.loyaltyapp.database.Notification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PreOrderViewModel extends AndroidViewModel {

    private final MutableLiveData<List<PreOrderListData>> items;
    private final MutableLiveData<List<PreOrderListData>> confirmationItems;
    private final MutableLiveData<Float> totalPriceLiveData;

    public PreOrderViewModel(@NonNull Application application) {
        super(application);
        LoyaltyDatabase database = LoyaltyDatabase.getDatabase(getApplication());
        LiveData<List<Item>> preprocessedItemsLiveData = database.itemDao().getAllItems();
        List<PreOrderListData> listOfItems = new ArrayList<>();
        items = new MutableLiveData<>(new ArrayList<>());
        confirmationItems = new MutableLiveData<>(new ArrayList<>());
        totalPriceLiveData = new MutableLiveData<>(0f);

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
        return cartItemDao.getAllCartItemForCart(cartId);
    }

    public void makeConfirmationPreOrderListData(List<CartItem> cartItems) {
        List<PreOrderListData> listOfItems = new ArrayList<PreOrderListData>();

        LoyaltyDatabase database = LoyaltyDatabase.getDatabase(getApplication());
        LiveData<List<Item>> preprocessedItemsLiveData = database.itemDao().getAllItems();

        preprocessedItemsLiveData.observeForever(new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> preprocessedItems) {
                float totalPrice = 0;
                for (Item item : preprocessedItems) {
                    for (CartItem cartItem : cartItems) {
                        if (item.getItemId() == cartItem.getItemId()) {
                            listOfItems.add(new PreOrderListData(item.getItemId(), item.getImage(), cartItem.getQuantity(), item.getPrice(), item.getName()));
                            totalPrice += item.getPrice() * cartItem.getQuantity();
                        }
                    }

                    totalPriceLiveData.setValue(totalPrice);
                    confirmationItems.setValue(listOfItems);

                }

                // Remove the observer right after getting a value since we will only be observing once
                preprocessedItemsLiveData.removeObserver(this);
            }
        });
    }

    public void createCart(String date) {
        CartDao cartDao = LoyaltyDatabase.getDatabase(getApplication()).cartDao();
        Cart cart = new Cart(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(R.string.user_id), -1), date);
        LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
            cartDao.insert(cart);
        });
    }

    public void initCartItems(Cart cart) {
        CartItemDao cartItemDao = LoyaltyDatabase.getDatabase(getApplication()).cartItemDao();
        LiveData<List<CartItem>> cartItemLiveData = cartItemDao.getAllCartItemForCart(cart.getId());
        cartItemLiveData.observeForever(new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                for (PreOrderListData preOrderListData : items.getValue()) {
                    boolean contains = false;
                    for (CartItem cartItem : cartItems) {
                        if (preOrderListData.getId() == cartItem.getItemId()) {
                            contains = true;
                            if (preOrderListData.getItemQuantity() == 0) {
                                LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                                    cartItemDao.deleteCartItem(cartItem);
                                });
                            } else {
                                cartItem.setQuantity(preOrderListData.getItemQuantity());
                                LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                                    cartItemDao.updateCartItem(cartItem);
                                });
                            }
                        }
                    }

                    if (preOrderListData.getItemQuantity() > 0 && !contains) {
                        CartItem newCartItem = new CartItem(cart.getId(), preOrderListData.getId(), preOrderListData.getItemQuantity());
                        LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                            cartItemDao.insert(newCartItem);
                        });
                    }
                }

                // Remove the observer right after getting a value since we will only be observing once
                cartItemLiveData.removeObserver(this);
            }
        });
    }

    public LiveData<Float> getTotalPrice() {
        return totalPriceLiveData;
    }

    public void incrementItemQuantity(int itemId, List<PreOrderListData> listData) {
        LiveData<Cart> cartLiveData = getCart();
        CartItemDao cartItemDao = LoyaltyDatabase.getDatabase(getApplication()).cartItemDao();
        cartLiveData.observeForever(new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                LiveData<List<CartItem>> cartItemLiveData = getCartItems(cart.getId());
                cartItemLiveData.observeForever(new Observer<List<CartItem>>() {
                    @Override
                    public void onChanged(List<CartItem> cartItems) {
                        for (CartItem cartItem : cartItems) {
                            if (cartItem.getItemId() == itemId) {
                                cartItem.setQuantity(cartItem.getQuantity() + 1);
                                LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                                    cartItemDao.updateCartItem(cartItem);
                                });
                            }
                        }

                        // Remove the observer right after getting a value since we will only be observing once
                        cartItemLiveData.removeObserver(this);
                    }

                });

                // Remove the observer right after getting a value since we will only be observing once
                cartLiveData.removeObserver(this);
            }
        });

        float totalPrice = 0;
        for (PreOrderListData preOrderListData : listData) {
            totalPrice += preOrderListData.getItemPrice() * preOrderListData.getItemQuantity();
        }
        totalPriceLiveData.setValue(totalPrice);

    }

    public void decrementItemQuantity(int itemId, List<PreOrderListData> listData) {
        LiveData<Cart> cartLiveData = getCart();
        CartItemDao cartItemDao = LoyaltyDatabase.getDatabase(getApplication()).cartItemDao();
        cartLiveData.observeForever(new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                LiveData<List<CartItem>> cartItemLiveData = getCartItems(cart.getId());
                cartItemLiveData.observeForever(new Observer<List<CartItem>>() {
                    @Override
                    public void onChanged(List<CartItem> cartItems) {
                        for (CartItem cartItem : cartItems) {
                            if (cartItem.getItemId() == itemId) {
                                cartItem.setQuantity(cartItem.getQuantity() - 1);
                                LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                                    cartItemDao.updateCartItem(cartItem);
                                });
                            }
                        }

                        // Remove the observer right after getting a value since we will only be observing once
                        cartItemLiveData.removeObserver(this);
                    }

                });

                // Remove the observer right after getting a value since we will only be observing once
                cartLiveData.removeObserver(this);
            }
        });

        float totalPrice = 0;
        for (PreOrderListData preOrderListData : listData) {
            totalPrice += preOrderListData.getItemPrice() * preOrderListData.getItemQuantity();
        }
        totalPriceLiveData.setValue(totalPrice);
    }

    public void updateBranch(String branch) {
        LiveData<Cart> cartLiveData = getCart();
        cartLiveData.observeForever(new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                cart.setLocation(branch);
                LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                    LoyaltyDatabase.getDatabase(getApplication()).cartDao().updateCart(cart);
                });

                // Remove the observer right after getting a value since we will only be observing once
                cartLiveData.removeObserver(this);
            }
        });
    }

    public void updateCollectionDate(String collectionDate) {
        LiveData<Cart> cartLiveData = getCart();
        cartLiveData.observeForever(new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                cart.setCollectionDate(collectionDate);
                LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                    LoyaltyDatabase.getDatabase(getApplication()).cartDao().updateCart(cart);
                });

                // Remove the observer right after getting a value since we will only be observing once
                cartLiveData.removeObserver(this);
            }
        });
    }

    public void completePreOrder() {
        LoyaltyDatabase database = LoyaltyDatabase.getDatabase(getApplication());
        LiveData<Cart> cartLiveData = getCart();
        cartLiveData.observeForever(new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                    database.cartItemDao().deleteAllFromCart(cart.getId());
                    database.cartDao().delete(cart);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(Calendar.getInstance().getTime());
                    database.notificationDao().insert(new Notification(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(R.string.user_id), -1),"Preorder Completed", "Please collect your preorder on the date specified.", formattedDate));
                });

                // Remove the observer right after getting a value since we will only be observing once
                cartLiveData.removeObserver(this);
            }
        });

        List<PreOrderListData> itemList = items.getValue();
        for (PreOrderListData preOrderListData : itemList) {
            preOrderListData.setItemQuantity(0);
        }

        items.setValue(itemList);
    }
}
