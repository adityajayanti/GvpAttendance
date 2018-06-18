package com.example.aditya.gvpattendance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Aditya on 28-02-2018.
 */

public class ContentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MainPageContents> contentlist;
    private OnItemClickListener mListener;
    private int ActivityviewType;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ContentsAdapter(Context context, List<MainPageContents> contentlist, int vt) {
        this.context = context;
        this.contentlist = contentlist;
        this.ActivityviewType = vt;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (ActivityviewType) {

            case 0:
                View view = inflater.inflate(R.layout.list_layout, null);
                return new ViewHolder0(view, mListener);

            case 1:
                View view1 = inflater.inflate(R.layout.subject_list_layout, null);
                return new ViewHolder2(view1, mListener);

            case 2:
                View view2 = inflater.inflate(R.layout.student_info, null);
                return new ViewHolder3(view2, mListener);

            case 3:
                View view3 = inflater.inflate(R.layout.student_attendance, null);
                return new ViewHolder4(view3, mListener);

            case 4:
                View view4 = inflater.inflate(R.layout.student_percent_show, null);
                return new ViewHolder5(view4, mListener);

            case 5:
                View view5 = inflater.inflate(R.layout.mysubject_layout, null);
                return new ViewHolder6(view5, mListener);

            case 6:
                View view6 = inflater.inflate(R.layout.mysubattendance_layout, null);
                return new ViewHolder7(view6, mListener);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MainPageContents mainPageContents = contentlist.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolder0 viewHolder0 = (ViewHolder0) holder;
                viewHolder0.textViewTitle.setText(mainPageContents.getTitle());
                viewHolder0.textViewDesc.setText(mainPageContents.getDesc());
                viewHolder0.imageView.setImageDrawable(context.getResources().getDrawable(mainPageContents.getImage()));
                viewHolder0.textView.setText("");
                break;

            case 1:
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                viewHolder2.textViewTitle.setText(mainPageContents.getTimehour());
                viewHolder2.textView.setText(" hour - " + mainPageContents.getDesc());
                viewHolder2.textViewDesc.setText(" " + mainPageContents.getTitle() + " " + String.valueOf(mainPageContents.getSem() + " Sem"));
                break;

            case 2:
                ViewHolder3 viewHolder3 = (ViewHolder3) holder;
                viewHolder3.textViewID.setText(mainPageContents.getStu_id());
                viewHolder3.textViewName.setText(mainPageContents.getStu_name());
                viewHolder3.textViewDept.setText(mainPageContents.getStu_dept());
                viewHolder3.textViewSem.setText(String.valueOf(mainPageContents.getStu_sem()));
                viewHolder3.textViewMob.setText(String.valueOf(mainPageContents.getStu_mob()));
                viewHolder3.textViewEmail.setText(mainPageContents.getStu_email());
                break;

            case 3:
                final ViewHolder4 viewHolder4 = (ViewHolder4) holder;
                viewHolder4.textViewID.setText(mainPageContents.getStu_id());
                viewHolder4.textViewName.setText(mainPageContents.getStu_name());
                viewHolder4.checkBox.setChecked(contentlist.get(position).get_PA());

                viewHolder4.checkBox.setTag(position);
                viewHolder4.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer pos = (Integer) viewHolder4.checkBox.getTag();
                        //Toast.makeText(context, contentlist.get(pos).getAnimal() + " clicked!", Toast.LENGTH_SHORT).show();

                        if (contentlist.get(pos).get_PA()) {
                            contentlist.get(pos).set_PA(false);
                        } else {
                            contentlist.get(pos).set_PA(true);
                        }
                    }
                });
                break;

            case 4:
                ViewHolder5 viewHolder5 = (ViewHolder5) holder;
                viewHolder5.textViewID.setText(mainPageContents.getStu_id());
                viewHolder5.textViewName.setText(mainPageContents.getStu_name());
                viewHolder5.textViewAtt.setText(mainPageContents.getTotal_att());
                viewHolder5.textViewTot.setText(mainPageContents.getTotal_held());
                viewHolder5.textViewPer.setText(mainPageContents.getTotal_per()+"%");
                break;

            case 5:
                ViewHolder6 viewHolder6 = (ViewHolder6) holder;
                viewHolder6.textViewSubName.setText(mainPageContents.getTitle())
                ;
                viewHolder6.textViewDept.setText(mainPageContents.getDesc());
                viewHolder6.textViewSem.setText(mainPageContents.getTimehour());
                break;

            case 6:
                ViewHolder7 viewHolder7 = (ViewHolder7) holder;
                viewHolder7.textViewDate.setText(mainPageContents.getTitle());
                viewHolder7.textViewHour.setText(mainPageContents.getDesc());
                viewHolder7.textViewTopic.setText(mainPageContents.getTimehour());
                viewHolder7.textViewAbs.setText(mainPageContents.getStu_id());
                break;
        }


    }

    @Override
    public int getItemCount() {
        return contentlist.size() > 0 ? contentlist.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        return ActivityviewType;
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewTitle, textViewDesc, textView;
        RelativeLayout relativeLayout;

        public ViewHolder0(View itemView, final OnItemClickListener listener) {

            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textViewCheck);
            relativeLayout = itemView.findViewById(R.id.relativelayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }

                    }
                }
            });
        }


    }

    class ViewHolder3 extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewID, textViewName, textViewDept , textViewSem , textViewMob , textViewEmail;
        RelativeLayout relativeLayout;

        public ViewHolder3(View itemView, final OnItemClickListener listener) {

            super(itemView);
            textViewID = itemView.findViewById(R.id.TextID);
            textViewName = itemView.findViewById(R.id.TextName);
            textViewDept = itemView.findViewById(R.id.TextDept);
            textViewSem = itemView.findViewById(R.id.TextSem);
            textViewMob = itemView.findViewById(R.id.TextMobile);
            textViewEmail = itemView.findViewById(R.id.TextEmail);
            relativeLayout = itemView.findViewById(R.id.relativelayout);
            relativeLayout.removeView(imageView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }

                    }
                }
            });
        }
    }

        class ViewHolder2 extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView textViewTitle, textViewDesc, textView;
            RelativeLayout relativeLayout;

            public ViewHolder2(View itemView, final OnItemClickListener listener) {

                super(itemView);
                textViewTitle = itemView.findViewById(R.id.textViewTitle);
                textViewDesc = itemView.findViewById(R.id.textViewDesc);
                imageView = itemView.findViewById(R.id.imageView);
                textView = itemView.findViewById(R.id.textViewCheck);
                relativeLayout = itemView.findViewById(R.id.relativelayout);
                relativeLayout.removeView(imageView);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onItemClick(position);
                            }

                        }
                    }
                });
            }


        }

    class ViewHolder4 extends RecyclerView.ViewHolder {

        ImageView imageView;
        CheckBox checkBox;
        TextView textViewID, textViewName;
        RelativeLayout relativeLayout;

        public ViewHolder4(View itemView, final OnItemClickListener listener) {

            super(itemView);
            textViewID = itemView.findViewById(R.id.TextID);
            textViewName = itemView.findViewById(R.id.TextName);
            checkBox = itemView.findViewById(R.id.Status_checkBox);
            relativeLayout = itemView.findViewById(R.id.relativelayout);
            //relativeLayout.removeView(imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }

                    }
                }
            });
        }
    }

    class ViewHolder5 extends RecyclerView.ViewHolder {

        TextView textViewID, textViewName, textViewAtt , textViewTot , textViewPer;
        RelativeLayout relativeLayout;

        public ViewHolder5(View itemView, final OnItemClickListener listener) {

            super(itemView);
            textViewID = itemView.findViewById(R.id.TextID);
            textViewName = itemView.findViewById(R.id.TextName);
            textViewAtt = itemView.findViewById(R.id.ValueAtt);
            textViewTot = itemView.findViewById(R.id.ValueTotal);
            textViewPer = itemView.findViewById(R.id.ValuePer);
            relativeLayout = itemView.findViewById(R.id.relativelayout);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }

                    }
                }
            });
        }
    }

    class ViewHolder6 extends RecyclerView.ViewHolder {

        TextView textViewSubName, textViewDept, textViewSem;
        RelativeLayout relativeLayout;

        public ViewHolder6(View itemView, final OnItemClickListener listener) {

            super(itemView);
            textViewSubName = itemView.findViewById(R.id.TextSubName);
            textViewDept = itemView.findViewById(R.id.TextDept);
            textViewSem = itemView.findViewById(R.id.TextSem);
            relativeLayout = itemView.findViewById(R.id.relativelayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }

                    }
                }
            });
        }


    }

    class ViewHolder7 extends RecyclerView.ViewHolder {

        TextView textViewDate, textViewHour, textViewTopic , textViewAbs;
        RelativeLayout relativeLayout;

        public ViewHolder7(View itemView, final OnItemClickListener listener) {

            super(itemView);
            textViewDate = itemView.findViewById(R.id.ValueDate);
            textViewHour = itemView.findViewById(R.id.ValueHour);
            textViewTopic = itemView.findViewById(R.id.ValueTopic);
            textViewAbs = itemView.findViewById(R.id.ValueAbsent);
            relativeLayout = itemView.findViewById(R.id.relativelayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }

                    }
                }
            });
        }


    }
}




