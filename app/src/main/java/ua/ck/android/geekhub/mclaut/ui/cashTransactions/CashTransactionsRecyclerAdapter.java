package ua.ck.android.geekhub.mclaut.ui.cashTransactions;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import org.joda.time.DateTime;

import butterknife.BindView;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.data.model.CashTransactionsEntity;
import ua.ck.android.geekhub.mclaut.tools.DateConverter;

/**
 * Created by bogda on 14.12.2017.
 */

public class CashTransactionsRecyclerAdapter  {
}
class TransactionsViewHolder extends RecyclerView.ViewHolder {
    public static final int TRANSACTION_TYPE_PAYMENT = 1;
    public static final int TRANSACTION_TYPE_WITHDRAW = 0;
    public static final int PAYMENT_TYPE_TERMINAL = 1;
    public static final int WITHDRAW_TYPE_PAY_AT_DAY = 1;

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

    public TransactionsViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
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
                break;
            case TRANSACTION_TYPE_WITHDRAW:
                transactionTypeTextView.setText(context.getString(R.string.label_transaction_type_withdraw));
                transactionTypeTextView.setTextColor(ContextCompat.getColor(context,
                        android.R.color.holo_red_dark));
                String summStringWithdraw = "-" + transaction.getSum() + context.getString(R.string.uah_symbol);
                transactionSummTextView.setText(summStringWithdraw);
                if(transaction.getType() == WITHDRAW_TYPE_PAY_AT_DAY){
                    typeTextView.setText(context.getString(R.string.pay_at_day_label));
                }
                else{
                    typeTextView.setText(context.getString(R.string.label_pay_type_other));
                }
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
