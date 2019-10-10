package com.lyloou.test.contact;

public class LinkMan {
    private String name;
    private String number;

    public LinkMan(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return name + '\n' +
                number + '\n';
    }

    @Override
    public int hashCode() {
        return this.number.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LinkMan)) {
            return false;
        }
        LinkMan man = (LinkMan) obj;
        return man.getNumber().equals(this.getNumber());
    }
}
