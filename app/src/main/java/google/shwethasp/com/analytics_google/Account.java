package google.shwethasp.com.analytics_google;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//import google.shwethasp.com.analytics_google.interfaces.IAccountInfo;

@DatabaseTable(tableName = "accounts")
public class Account  {
    @DatabaseField
    private boolean close;
    private boolean favorite;
    @DatabaseField
    private String lastProfileId;
    @DatabaseField(id = true)
    private String name;
    @ForeignCollectionField(eager = false)
//    private Collection<Profile> profiles;
    @DatabaseField
    private String token;

    public Account(String accountName, String token) {
        this.name = accountName;
        this.token = token;
    }

    public Account(String accountName) {
        this.name = accountName;
    }



    public String getName() {
        return this.name;
    }

    public void setName(String accountName) {
        this.name = accountName;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

   /* public List<Profile> getProfiles() {
        if (this.profiles == null) {
            return null;
        }
        return new ArrayList(this.profiles);
    }

    public void setProfiles(Collection<Profile> profiles) {
        this.profiles = profiles;
    }*/

    public String getLastProfileId() {
        return this.lastProfileId;
    }

    public void setLastProfileId(String lastProfileId) {
        this.lastProfileId = lastProfileId;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return this.favorite;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Account)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        Account accountInfo = (Account) o;
        if (this.name != null) {
            return this.name.equals(accountInfo.getName());
        }
        return false;
    }

    public Account clone() {
        Account clone = new Account(this.name);
        clone.token = this.token;
//        clone.profiles = this.profiles;
        clone.close = this.close;
        return clone;
    }
}
