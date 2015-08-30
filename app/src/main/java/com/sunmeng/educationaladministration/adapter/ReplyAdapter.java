package com.sunmeng.educationaladministration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.sunmeng.educationaladministration.R;

import java.util.List;
import java.util.Map;

/**
 * Created by YUZEPENG on 2015/8/26.
 */
public class ReplyAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<Map<String, Object>> list;

    /**xx
     * HttpUtils
     */
    HttpUtils mHttpUtils;

    public ReplyAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mHttpUtils = new HttpUtils();
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();

    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.item_reply_curricular, null);
        TextView tv_date_reply = (TextView) view.findViewById(R.id.tv_date_reply);
        TextView tv_teacher_reply = (TextView) view.findViewById(R.id.tv_teacher_reply);
        TextView tv_number_reply = (TextView) view.findViewById(R.id.tv_number_reply);
        TextView tv_course_reply = (TextView) view.findViewById(R.id.tv_course_reply);
        TextView tv_address_reply = (TextView) view.findViewById(R.id.tv_address_reply);

        Map<String, Object> map = list.get(position);
                tv_date_reply.setText(map.get("rdate").toString());
                tv_teacher_reply.setText(map.get("tename").toString());
                tv_number_reply.setText(map.get("cnname").toString());
                tv_course_reply.setText(map.get("rcourse").toString());
                tv_address_reply.setText(map.get("raddress").toString());

        return view;
    }
}
