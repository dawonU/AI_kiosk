package com.example.kiosk;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddCart extends RecyclerView.Adapter<AddCart.ViewHolder> {

    // 콜백 인터페이스 선언
    public interface OnCartItemChangeListener {
        void onCartItemChanged();
    }

    private OnCartItemChangeListener onCartItemChangeListener;

    public void setOnCartItemChangeListener(OnCartItemChangeListener listener) {
        this.onCartItemChangeListener = listener;
    }

    public static class CartItem {
        private String menuName;
        private int subSetPrice;
        private int subLSetPrice;
        // 기본 수량은 1로 설정
        private int quantity = 1;

        public CartItem(String menuName, int subSetPrice, int subLSetPrice) {
            this.menuName = menuName;
            this.subSetPrice = subSetPrice;
            this.subLSetPrice = subLSetPrice;
        }

        public String getMenuName() {
            return menuName;
        }

        public int getSubSetPrice() {
            return subSetPrice;
        }

        public int getSubLSetPrice() {
            return subLSetPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    private Context context;
    private List<CartItem> cartItemList;

    public AddCart(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton removeButton;
        public TextView itemName;
        public TextView itemQuantity;
        public ImageButton decrementButton;
        public ImageButton incrementButton;
        public TextView priceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            removeButton = itemView.findViewById(R.id.remove_button);
            itemName = itemView.findViewById(R.id.item_name);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            decrementButton = itemView.findViewById(R.id.decrement_button);
            incrementButton = itemView.findViewById(R.id.increment_button);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }
    }

    @Override
    public AddCart.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddCart.ViewHolder holder, int position) {
        CartItem currentItem = cartItemList.get(position);
        holder.itemName.setText(currentItem.getMenuName());

        String priceText;
        if (currentItem.getSubSetPrice() != 0) {
            priceText = currentItem.getSubSetPrice() + "원";
        } else if (currentItem.getSubLSetPrice() != 0) {
            priceText = currentItem.getSubLSetPrice() + "원";
        } else {
            priceText = "가격 없음";
        }
        holder.priceTextView.setText(priceText);
        holder.itemQuantity.setText(String.valueOf(currentItem.getQuantity()));

        holder.decrementButton.setOnClickListener(v -> {
            int quantity = currentItem.getQuantity();
            if (quantity > 1) {
                currentItem.setQuantity(quantity - 1);
                holder.itemQuantity.setText(String.valueOf(currentItem.getQuantity()));
                if (onCartItemChangeListener != null) {
                    onCartItemChangeListener.onCartItemChanged();
                }
            }
        });

        holder.incrementButton.setOnClickListener(v -> {
            int quantity = currentItem.getQuantity();
            currentItem.setQuantity(quantity + 1);
            holder.itemQuantity.setText(String.valueOf(currentItem.getQuantity()));
            if (onCartItemChangeListener != null) {
                onCartItemChangeListener.onCartItemChanged();
            }
        });

        holder.removeButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            cartItemList.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, cartItemList.size());
            if (onCartItemChangeListener != null) {
                onCartItemChangeListener.onCartItemChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public void addItem(String menuName, int subSetPrice, int subLSetPrice) {
        CartItem newItem = new CartItem(menuName, subSetPrice, subLSetPrice);
        cartItemList.add(newItem);
        notifyItemInserted(cartItemList.size() - 1);
    }
}
