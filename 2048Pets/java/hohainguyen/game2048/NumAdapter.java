package hohainguyen.game2048;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/26/2018.
 */

public class NumAdapter extends ArrayAdapter<Integer> {
    private  Context ct;
    private  ArrayList<Integer> arr;

    public NumAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Integer> objects) {
        super(context, resource, objects);
        this.ct = context;
        this.arr = new ArrayList<>(objects);
    }
    @Override
    public  void notifyDataSetChanged(){
        arr= Data.getData().getArr();
        super.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView== null){
            LayoutInflater inflater = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_square,null);
        }

        if(arr.size()>0){
            Square square = (Square)convertView.findViewById(R.id.txtSquare);
            square.setTett(+arr.get(position));
        }

        return  convertView;
    }
}
