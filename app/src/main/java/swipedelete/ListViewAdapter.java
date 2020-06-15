package swipedelete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import swipedelete.view.SwipeMenuLayout;

import java.util.List;

/**
 * ListView的adapter
 */
public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<TestBean> lists;//数据源
    private LayoutInflater inflater;

    public ListViewAdapter(Context context, List<TestBean> lists) {
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return lists != null ? lists.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_index_previous,parent,false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.listview_tv);
            //holder.tvInfo = (TextView) convertView.findViewById(R.id.listview_tv);
            holder.ivImg = (ImageView) convertView.findViewById(R.id.listview_iv);
            holder.btnZD = (Button) convertView.findViewById(R.id.btn_zd);
            holder.btnDelete = (Button) convertView.findViewById(R.id.btn_delete);
            holder.llContent = (LinearLayout) convertView.findViewById(R.id.ll_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        TestBean bean = lists.get(position);
        holder.tvName.setText(bean.getName());
        holder.ivImg.setImageResource(bean.getImgRes());
        holder.tvInfo.setText(bean.getInfo());

        //根据自己需求设置一些限制条件，比如这里设置了：IOS效果阻塞，item依次是左滑、右滑
        ((SwipeMenuLayout)convertView).setIos(true).setLeftSwipe(position % 2 == 0 ? true : false);
        //监听事件
        holder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击了："+position, Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnZD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击了置顶选项", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"删除了："+position, Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    class ViewHolder{
        private TextView tvName;
        private TextView tvInfo;
        private ImageView ivImg;
        private Button btnZD;
        private Button btnDelete;
        private LinearLayout llContent;
    }


}
