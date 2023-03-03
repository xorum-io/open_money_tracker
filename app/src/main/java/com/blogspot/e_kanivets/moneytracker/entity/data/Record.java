package com.blogspot.e_kanivets.moneytracker.entity.data;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import com.blogspot.e_kanivets.moneytracker.entity.base.BaseEntity;

/**
 * Entity class. Immutable.
 * Created by eugene on 01/09/14.
 */
public class Record extends BaseEntity implements Parcelable {
    public static final int TYPE_INCOME = 0;
    public static final int TYPE_EXPENSE = 1;

    private final long time;
    private final int type;
    private final String title;
    private final Category category;
    private final String notes;
    private final long price;
    private final Account account;
    private final String currency;
    private final long decimals;

    public Record(long id, long time, int type, String title, long categoryId, String notes,
                  long price, long accountId, String currency, long decimals) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.title = title;
        this.category = new Category(categoryId, null);
        this.notes = notes;
        this.price = price;
        this.account = new Account(accountId, null, -1, null, 0, -1, false, -1);
        this.currency = currency;
        this.decimals = decimals;
    }

    public Record(long id, long time, int type, String title, Category category, String notes,
                  long price, Account account, String currency, long decimals) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.title = title;
        this.category = category;
        this.notes = notes;
        this.price = price;
        this.account = account;
        this.currency = currency;
        this.decimals = decimals;
    }

    public Record(long id, long time, int type, String title, Category category, String notes,
                  double price, Account account, String currency) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.title = title;
        this.category = category;
        this.notes = notes;
        this.account = account;
        this.currency = currency;
        this.price = getLong(price);
        this.decimals = getDecimal(price);
    }

    public Record(long time, int type, String title, Category category, String notes, double price,
                  Account account, String currency) {
        this.id = -1;
        this.time = time;
        this.type = type;
        this.title = title;
        this.category = category;
        this.notes = notes;
        this.account = account;
        this.currency = currency;
        this.price = getLong(price);
        this.decimals = getDecimal(price);
    }

    protected Record(Parcel in) {
        id = in.readLong();
        time = in.readLong();
        type = in.readInt();
        title = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        notes = in.readString();
        price = in.readLong();
        account = in.readParcelable(Account.class.getClassLoader());
        currency = in.readString();
        decimals = in.readLong();
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    public int getType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    public Category getCategory() {
        return category;
    }


    public String getNotes() { return  notes; }

    public long getPrice() {
        return price;
    }

    public long getDecimals() {
        return decimals;
    }

    public double getFullPrice() {
        return price + decimals / 100.0;
    }

    public long getTime() {
        return time;
    }

    public boolean isIncome() {
        return type == 0;
    }

    @Nullable
    public Account getAccount() {
        return account;
    }

    public String getCurrency() {
        if (currency == null) return DbHelper.DEFAULT_ACCOUNT_CURRENCY;
        else return currency;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Record {");
        sb.append("id = ").append(id).append(", ");
        sb.append("title = ").append(title).append(", ");

        sb.append("type = ");
        switch (type) {
            case TYPE_EXPENSE:
                sb.append("expense");
                break;

            case TYPE_INCOME:
                sb.append("income");
                break;

            default:
                sb.append("unknown");
                break;
        }
        sb.append(", ");

        sb.append("time = ").append(time).append(", ");
        sb.append("category = ").append(category).append(", ");
        sb.append("notes = ").append(notes).append(", ");
        sb.append("price = ").append(price).append(", ");
        sb.append("account = ").append(account).append(", ");
        sb.append("currency = ").append(currency).append(", ");
        sb.append("decimals = ").append(decimals);
        sb.append("}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Record) {
            Record record = (Record) o;
            return this.id == record.getId()
                    && this.time == record.getTime()
                    && this.type == record.getType()
                    && equals(this.title, record.getTitle())
                    && this.category.equals(record.getCategory())
                    && equals(this.notes, record.getNotes())
                    && this.price == record.getPrice()
                    && this.account.equals(record.getAccount())
                    && equals(this.currency, record.getCurrency())
                    && this.decimals == record.decimals;
        } else return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(time);
        dest.writeInt(type);
        dest.writeString(title);
        dest.writeParcelable(category, 0);
        dest.writeString(notes);
        dest.writeLong(price);
        dest.writeParcelable(account, 0);
        dest.writeString(currency);
        dest.writeLong(decimals);
    }
}
