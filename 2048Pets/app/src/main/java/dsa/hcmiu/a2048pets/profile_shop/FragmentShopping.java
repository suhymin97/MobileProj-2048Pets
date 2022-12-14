package dsa.hcmiu.a2048pets.profile_shop;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import dsa.hcmiu.a2048pets.MyApplication;
import dsa.hcmiu.a2048pets.R;
import dsa.hcmiu.a2048pets.entities.adapter.ShopAdapter;
import dsa.hcmiu.a2048pets.entities.handle.HandleFile;
import dsa.hcmiu.a2048pets.entities.handle.HandleGame;
import dsa.hcmiu.a2048pets.entities.model.Features;
import dsa.hcmiu.a2048pets.entities.model.ShopItem;

import static dsa.hcmiu.a2048pets.entities.model.Features.shopItem;
import static dsa.hcmiu.a2048pets.entities.model.Features.user;

public class FragmentShopping extends Fragment {
    private ArrayList<ShopItem> arrayShopItem;
    ShopAdapter adapter;
    TextView tvGold,tvPrice, tvItemName;
    GridView listItem;
    ImageView ivShopItem;
    ImageButton btnPurchase;
    ShopItem selectItem;
    SendData sendData;

    public FragmentShopping() {
        super(R.layout.fragment_store);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store,container,false);
        init();
        checkPurchased();
        selectItem = arrayShopItem.get(0);
        adapter = new ShopAdapter(getActivity(), R.layout.item_shop, arrayShopItem);

        listItem = (GridView) view.findViewById(R.id.lvShopping);
        tvGold = (TextView) view.findViewById(R.id.tvAchiveGold);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice) ;
        tvItemName = (TextView) view.findViewById(R.id.tvNameItem) ;
        ivShopItem = (ImageView) view.findViewById(R.id.ivShopItem);
        btnPurchase = (ImageButton) view.findViewById(R.id.btnPurchase);
        sendData = (SendData) getActivity();

        showItem();
        update();
        listItem.setAdapter(adapter);
//		listItem.setOnItemClickListener((parent, view1, position, id) -> { //duc
//            RelativeLayout relativeLayout= (RelativeLayout) view1;
//            relativeLayout.setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.white));
//            selectItem = arrayShopItem.get(position);
//            showItem();
//        });
        listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() { //avu7
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RelativeLayout relativeLayout= (RelativeLayout) view;
                relativeLayout.setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.white));
                selectItem = arrayShopItem.get(position);
                showItem();
            }
        });
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((user.totalGold < selectItem.getPrice() ) && (!selectItem.isPurchase())) {
                    Toast.makeText(getActivity(),"Not enough gold. Play 2048 to earn more.",Toast.LENGTH_LONG).show();
                    return;
                }
                user.totalGold -= selectItem.getPrice();
                switch((int) selectItem.getId()/100) {
                    case 0: //undo
                        user.undo++;
                        break;
                    case 1: //hammer
                        user.hammer++;
                        break;
                    case 2: //ava
                        if (selectItem.isPurchase() || user.purchasedIdItem.contains(selectItem.getId())) {
                            user.setAvatar(selectItem.getPicture());
                            return;
                        }
                        selectItem.setPurchased();
                        user.purchasedIdItem.add(selectItem.getId());
                        break;
                    case 3: //theme
                        if (selectItem.isPurchase() || user.purchasedIdItem.contains(selectItem.getId())) {
                            Features.theme = selectItem.getId()%100;
                            HandleGame.getInstance().setTheme(Features.theme);
                            return;
                        }
                        selectItem.setPurchased();
                        user.purchasedIdItem.add(selectItem.getId());
                        break;
                    case 4: //fb ava
                        if (!user.isLogged()) {
                            Toast.makeText(getActivity(),"You haven't connect with facebook",Toast.LENGTH_LONG).show();
                            user.totalGold += selectItem.getPrice();
                            break;
                        }
                        if (selectItem.isPurchase()) {
                            user.setAvatar(0);
                            selectItem.setPurchase(false);
                            user.unPurcahsedIdItem(selectItem.getId());
                            user.totalGold += selectItem.getPrice();
                            return;
                        }
                        selectItem.setPurchase(true);
                        user.purchasedIdItem.add(selectItem.getId());
                        break;
                }
                showItem();
                sendData.dataProfile(true);
                update();

            }
        });

        if(!user.isLogged()){
            init();
        }
        return view;
    }

    private void addItem(String name, int id, int picture, long price) {
        arrayShopItem.add(new ShopItem(name, id, picture, price));
    }

    private void init() {
        arrayShopItem = new ArrayList<>();
        addItem("undo", 000, R.drawable.undo, 100);
        addItem("hammer", 100, R.drawable.hammer, 1000);
        addItem("Avatar Default", 200, R.drawable.default_ava, 0);
        addItem("Avatar Pikachu", 201, R.drawable.pikachu2, 200);
        addItem("Avatar Meowth", 202, R.drawable.poke, 300);
        addItem("Avatar Dog", 203, R.drawable.ic_round, 300);
        addItem("Avatar Profile Facebook", 400, R.drawable.facebook, 1000);
        addItem("Theme Classic", 300, R.raw.no2_1, 0);
        addItem("Theme Cat Items", 301, R.drawable.theme2, 2000);
    }
    private void checkPurchased() {
        for(ShopItem item: arrayShopItem) {
//            for(int idPurchased: user.purchasedIdItem) {
//                if (idPurchased != item.getId()) continue;
//                Log.d("Shop", "update: itemid " + item.getId() + " idPurchased" + idPurchased);
//                if (item.getId() == 400) item.setPurchase(true);
//                else item.setPurchased();
//            }
            if (user.purchasedIdItem.contains(item.getId())) {
                Log.d("Shop", "update: idPurchased" + item.getId());
                item.setPurchased();
            }
            if (item.getId() == 400) item.setPurchase(true);
        }
    }

    public void showItem() {
        tvPrice.setText(String.valueOf(selectItem.getPrice()));
        ivShopItem.setImageResource(selectItem.getPicture());
        tvItemName.setText(String.valueOf(selectItem.getName()));
        if (selectItem.isPurchase()) btnPurchase.setImageResource(R.drawable.equip);
        else btnPurchase.setImageResource(R.drawable.purchase);
    }

    private void update() {
        checkPurchased();
        tvGold.setText(String.valueOf(user.totalGold));
        try {
            HandleFile.writeUserToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    public void onChangeData() {
        tvGold.setText(String.valueOf(user.totalGold));
    }

    public void reset(){
        shopItem.returnDefault();
    }
}
