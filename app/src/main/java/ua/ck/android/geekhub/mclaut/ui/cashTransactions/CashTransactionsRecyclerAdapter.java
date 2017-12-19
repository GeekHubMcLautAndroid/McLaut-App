package ua.ck.android.geekhub.mclaut.ui.cashTransactions;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.data.model.CashTransactionsEntity;
import ua.ck.android.geekhub.mclaut.tools.DateConverter;

/**
 * Created by bogda on 14.12.2017.
 */

public class CashTransactionsRecyclerAdapter extends RecyclerView.Adapter<TransactionsViewHolder> {
    private List<CashTransactionsEntity> cashTransactionsInfoList = new ArrayList<>();

    public CashTransactionsRecyclerAdapter(List<CashTransactionsEntity> cashTransactionsInfoList) {
        this.cashTransactionsInfoList = cashTransactionsInfoList;
    }

    @Override
    public TransactionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TransactionsViewHolder holder;
        View layoutView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_transaction_info,parent,false);
        holder = new TransactionsViewHolder(parent.getContext(),layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(TransactionsViewHolder holder, int position) {
        holder.bind(cashTransactionsInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        if(cashTransactionsInfoList != null){
            return cashTransactionsInfoList.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
class TransactionsViewHolder extends RecyclerView.ViewHolder {
    private static final int TRANSACTION_TYPE_PAYMENT = 1;
    private static final int TRANSACTION_TYPE_WITHDRAW = 0;
    private static final int PAYMENT_TYPE_TERMINAL = 1;
    private static final int WITHDRAW_TYPE_PAY_AT_DAY = 1;

    private Context context;

    @BindView(R.id.list_item_transactions_transaction_type)
    TextView transactionTypeTextView;
    @BindView(R.id.list_item_transactions_date)
    TextView transactionDateTextView;
    @BindView(R.id.list_item_transactions_summ)
    TextView transactionSummTextView;
    @BindView(R.id.list_item_transactions_type)
    TextView typeTextView;
    @BindView(R.id.list_item_transactions_balance_after)
    TextView balanceAfterTextView;

    TransactionsViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this,itemView);
    }

    void bind(CashTransactionsEntity transaction){
        switch (transaction.getTypeOfTransaction()){
            case TRANSACTION_TYPE_PAYMENT:
                transactionTypeTextView.setText(context.getString(R.string.label_transaction_type_payment));
                transactionTypeTextView.setTextColor(ContextCompat.getColor(context,
                        android.R.color.holo_green_dark));
                String summStringPayment = "+" + transaction.getSum() + context.getString(R.string.uah_symbol);
                transactionSummTextView.setText(summStringPayment);
                if(transaction.getType() == PAYMENT_TYPE_TERMINAL){
                    typeTextView.setText(context.getString(R.string.label_pay_type_terminal));
                }
                else{
                    typeTextView.setText(context.getString(R.string.label_pay_type_other));
                }
                double balanceAfter = transaction.getSumBefore() + transaction.getSum();
                balanceAfterTextView.setText(context.getString(R.string.label_text_after_transaction,
                        balanceAfter));
                break;
            case TRANSACTION_TYPE_WITHDRAW:
                transactionTypeTextView.setText(context.getString(R.string.label_transaction_type_withdraw));
                transactionTypeTextView.setTextColor(ContextCompat.getColor(context,
                        android.R.color.holo_red_dark));
                String summStringWithdraw = "-" + transaction.getSum() + context.getString(R.string.uah_symbol);
                transactionSummTextView.setText(summStringWithdraw);
                if(transaction.getType() == WITHDRAW_TYPE_PAY_AT_DAY){
                    typeTextView.setText(context.getString(R.string.label_pay_type_pay_at_day));
                }
                else{
                    typeTextView.setText(context.getString(R.string.label_pay_type_other));
                }
                balanceAfterTextView.setText(context.getString(R.string.label_text_after_transaction,
                        (transaction.getSumBefore() - transaction.getSum())));
                break;
        }
        DateTime transactionDate = DateConverter.fromTimestampToDateTime(transaction.getDate());
        String dateTime = transactionDate.getDayOfMonth() + "/" +
                transactionDate.getMonthOfYear() + "/" +
                transactionDate.getYear() + " " +
                transactionDate.getHourOfDay() + ":" +
                transactionDate.getMinuteOfHour();
        transactionDateTextView.setText(dateTime);

    }
}
