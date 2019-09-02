package com.alisha.roomfinderapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Room implements Parcelable {
    public String id;
    public String name;
    public String desc;
    public String location;
    public String image;
    public String category_name;
    public long contact_no;
    public String price;
    public String perUnits;
    public String owner_name;
    public String owner_id;
    public long no_of_rooms;
    public boolean isFullFlat;
    public String ownerRules;
    public String district;
    public String exactLocation;// Kathmandu, Ason 132, Nepal
    public String date_added;
    public String updated_at;
    public String deleted_at;
    public long longitutde;
    public long lattitude;
    public long rating;
    public long viewCount;
    public String onlinePaymentType;
    public String services;


    public Room() {
    }

    protected Room(Parcel in) {
        id = in.readString();
        name = in.readString();
        desc = in.readString();
        location = in.readString();
        image = in.readString();
        category_name = in.readString();
        contact_no = in.readLong();
        price = in.readString();
        perUnits = in.readString();
        owner_name = in.readString();
        owner_id = in.readString();
        no_of_rooms = in.readLong();
        isFullFlat = in.readByte() != 0;
        ownerRules = in.readString();
        district = in.readString();
        exactLocation = in.readString();
        date_added = in.readString();
        updated_at = in.readString();
        deleted_at = in.readString();
        longitutde = in.readLong();
        lattitude = in.readLong();
        rating = in.readLong();
        viewCount = in.readLong();
        onlinePaymentType = in.readString();
        services = in.readString();
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public long getContact_no() {
        return contact_no;
    }

    public void setContact_no(long contact_no) {
        this.contact_no = contact_no;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPerUnits() {
        return perUnits;
    }

    public void setPerUnits(String perUnits) {
        this.perUnits = perUnits;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public long getNo_of_rooms() {
        return no_of_rooms;
    }

    public void setNo_of_rooms(long no_of_rooms) {
        this.no_of_rooms = no_of_rooms;
    }

    public boolean isFullFlat() {
        return isFullFlat;
    }

    public void setFullFlat(boolean fullFlat) {
        isFullFlat = fullFlat;
    }

    public String getOwnerRules() {
        return ownerRules;
    }

    public void setOwnerRules(String ownerRules) {
        this.ownerRules = ownerRules;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getExactLocation() {
        return exactLocation;
    }

    public void setExactLocation(String exactLocation) {
        this.exactLocation = exactLocation;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public long getLongitutde() {
        return longitutde;
    }

    public void setLongitutde(long longitutde) {
        this.longitutde = longitutde;
    }

    public long getLattitude() {
        return lattitude;
    }

    public void setLattitude(long lattitude) {
        this.lattitude = lattitude;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public String getOnlinePaymentType() {
        return onlinePaymentType;
    }

    public void setOnlinePaymentType(String onlinePaymentType) {
        this.onlinePaymentType = onlinePaymentType;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(desc);
        parcel.writeString(location);
        parcel.writeString(image);
        parcel.writeString(category_name);
        parcel.writeLong(contact_no);
        parcel.writeString(price);
        parcel.writeString(perUnits);
        parcel.writeString(owner_name);
        parcel.writeString(owner_id);
        parcel.writeLong(no_of_rooms);
        parcel.writeByte((byte) (isFullFlat ? 1 : 0));
        parcel.writeString(ownerRules);
        parcel.writeString(district);
        parcel.writeString(exactLocation);
        parcel.writeString(date_added);
        parcel.writeString(updated_at);
        parcel.writeString(deleted_at);
        parcel.writeLong(longitutde);
        parcel.writeLong(lattitude);
        parcel.writeLong(rating);
        parcel.writeLong(viewCount);
        parcel.writeString(onlinePaymentType);
        parcel.writeString(services);
    }
}
