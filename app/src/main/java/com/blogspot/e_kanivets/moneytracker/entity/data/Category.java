package com.blogspot.e_kanivets.moneytracker.entity.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.blogspot.e_kanivets.moneytracker.entity.base.BaseEntity;

/**
 * Entity class for account.
 * Created on 01/09/14.
 *
 * @author Evgenii Kanivets
 */
public class Category extends BaseEntity implements Parcelable {
    private String name;

    public Category(long id, String name) {
        setId(id);
        this.name = name;
    }

    public Category(String name) {
        setId(-1L);
        this.name = name;
    }

    protected Category(Parcel in) {
        setId(in.readLong());
        name = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Category) {
            Category category = (Category) o;
            return this.getId() == category.getId()
                    && equals(this.name, category.getName());
        } else return false;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Category {");
        sb.append("id = ").append(getId()).append(", ");
        sb.append("title = ").append(name);
        sb.append("}");

        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(name);
    }
}