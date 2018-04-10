package ce.inu.ikta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NyuNyu on 2018-04-09.
 */

public class Adapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> list;
    private HashMap<String,ArrayList<String>> child;

    public Adapter(Context context, ArrayList<String> list, HashMap<String, ArrayList<String>> child) {
        super();
        this.context=context;
        this.list=list;     //resultActivity에서 만들었던 배열의 생성자
        this.child=child;
    }


    @Override   //상위 그룹의 크기
    public int getGroupCount() {
        return list.size();
    }

    @Override   //하위 그룹의 크기
    public int getChildrenCount(int groupPosition) {
        return child.get(list.get(groupPosition)).size();
    }

    @Override   //상위 그룹의 값을 가져옴
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override   //하위 그룹의 값을 가져옴
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(list.get(groupPosition)).get(childPosition);
    }

    @Override   //상위 그룹의 id를 가져옴
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override   //하위 그룹의 id를 가져옴
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override   //상위그룹 data
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //리스트에 넣어줄 뷰를 리턴할 함수를 정함
        String listname = list.get( groupPosition );
        View view = convertView;

        //뷰 초기화
        if(view==null) {
            //상위 그룹 뷰의 xml 생성
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = inflater.inflate( R.layout.result_group, null );
        }

        TextView textView = (TextView) view.findViewById( R.id.group_txt);
        textView.setText(listname);

        return view;
    }

    @Override   //하위그룹 data 넣기
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childlist = child.get( list.get( groupPosition ) ).get( childPosition );
        View view = convertView;

        if (view == null) {
            //하위 그룹 뷰의 xml 생성
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = inflater.inflate( R.layout.result_child, null );
        }
        TextView textChild = (TextView) view.findViewById( R.id.child );
        textChild.setText( childlist );

        return view;
    }

    @Override   //하위 그룹을 클릭할 것인지
    public boolean isChildSelectable( int groupPosition, int childPosition) {
        return true;
    }

    //?????
    public boolean areAllItemsEnable() {
        return super.areAllItemsEnabled();
    }

    @Override   //같은 data 값을 생략시켜줌
    public boolean hasStableIds() {
        return false;
    }
}
