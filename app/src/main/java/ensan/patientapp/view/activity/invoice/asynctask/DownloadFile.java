package ensan.patientapp.view.activity.invoice.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import ensan.patientapp.R;
import ensan.patientapp.Utils.FileDownloader;
import ensan.patientapp.Utils.Progress;

public class DownloadFile extends AsyncTask<String, Void, Void> {

    private Progress progress;
    private boolean isStatus;
    private Context context;
    private File file;

    public DownloadFile(Context context, Progress progress){
        this.progress = progress;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... strings) {
        String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
        String fileName = strings[1];  // -> maven.pdf
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, "ensan_patient");
        folder.mkdir();
        File pdfFile = new File(folder, fileName);

        try{
            pdfFile.createNewFile();
            this.file = folder;
        }catch (IOException e){
            e.printStackTrace();
        }
         isStatus = FileDownloader.downloadFile(fileUrl, pdfFile);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progress.hide();
        if(isStatus){
            Toast.makeText(context, file.toString(), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,context.getString(R.string.downloadingerror), Toast.LENGTH_SHORT).show();
        }
    }
}