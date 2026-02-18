package com.blogspot.e_kanivets.moneytracker.entity.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.blogspot.e_kanivets.moneytracker.entity.base.BaseEntity;

/**
 * Entity class to represent transfer operation between accounts.
 * Created on 2/10/16.
 *
 * @author Evgenii Kanivets
 */
public class Transfer extends BaseEntity implements Parcelable {
    private final long time;
    private final long fromAccountId;
    private final long toAccountId;
    private final long fromAmount;
    private final long toAmount;
    private final long fromDecimals;
    private final long toDecimals;

    public Transfer(long id, long time, long fromAccountId, long toAccountId, long fromAmount,
                    long toAmount, long fromDecimals, long toDecimals) {
        this.fromDecimals = fromDecimals;
        this.toDecimals = toDecimals;
        setId(id);
        this.time = time;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
    }

    public Transfer(long time, long fromAccountId, long toAccountId, double fromAmount, double toAmount) {
        this.time = time;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.fromAmount = getLong(fromAmount);
        this.fromDecimals = getDecimal(fromAmount);
        this.toAmount = getLong(toAmount);
        this.toDecimals = getDecimal(toAmount);
    }

    protected Transfer(Parcel in) {
        time = in.readLong();
        fromAccountId = in.readLong();
        toAccountId = in.readLong();
        fromAmount = in.readLong();
        toAmount = in.readLong();
        fromDecimals = in.readLong();
        toDecimals = in.readLong();
    }

    public static final Creator<Transfer> CREATOR = new Creator<Transfer>() {
        @Override
        public Transfer createFromParcel(Parcel in) {
            return new Transfer(in);
        }

        @Override
        public Transfer[] newArray(int size) {
            return new Transfer[size];
        }
    };

    public long getTime() {
        return time;
    }

    public long getFromAccountId() {
        return fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public long getFromAmount() {
        return fromAmount;
    }

    public long getToAmount() {
        return toAmount;
    }

    public long getFromDecimals() {
        return fromDecimals;
    }

    public long getToDecimals() {
        return toDecimals;
    }

    public double getFullFromAmount() {
        return fromAmount + fromDecimals / 100.0;
    }

    public double getFullToAmount() {
        return toAmount + toDecimals / 100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Transfer) {
            Transfer transfer = (Transfer) o;
            return this.getId() == transfer.getId()
                    && this.time == transfer.time
                    && this.fromAccountId == transfer.fromAccountId
                    && this.toAccountId == transfer.toAccountId
                    && this.fromAmount == transfer.fromAmount
                    && this.toAmount == transfer.toAmount
                    && this.fromDecimals == transfer.fromDecimals
                    && this.toDecimals == transfer.toDecimals;
        } else return false;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transfer {");
        sb.append("id = ").append(getId()).append(", ");
        sb.append("time = ").append(time).append(", ");
        sb.append("fromAccountId = ").append(fromAccountId).append(", ");
        sb.append("toAccountId = ").append(toAccountId).append(", ");
        sb.append("fromAmount = ").append(fromAmount).append(", ");
        sb.append("toAmount = ").append(toAmount).append(", ");
        sb.append("fromDecimals = ").append(fromDecimals).append(", ");
        sb.append("toDecimals = ").append(toDecimals);
        sb.append("}");

        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeLong(fromAccountId);
        dest.writeLong(toAccountId);
        dest.writeLong(fromAmount);
        dest.writeLong(toAmount);
        dest.writeLong(fromDecimals);
        dest.writeLong(toDecimals);
    }
}
