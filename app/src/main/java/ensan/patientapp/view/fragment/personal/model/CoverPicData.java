package ensan.patientapp.view.fragment.personal.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoverPicData {

    @SerializedName("cover_pic")
    @Expose
    private String coverPic;

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }
}
