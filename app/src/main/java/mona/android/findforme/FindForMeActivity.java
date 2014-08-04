package mona.android.findforme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.squareup.otto.Bus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mona.android.findforme.tasks.PhotoUploadTask;
import mona.android.findforme.tasks.PhotoUploadTaskQueue;

public class FindForMeActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @InjectView(R.id.fl_take_photo) private FrameLayout mFlTakePhoto;

    @Inject private Bus mBus;
    @Inject private PhotoUploadTaskQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((FindForMeApplication) getApplication()).inject(this);

        setContentView(R.layout.find_for_me);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.fl_take_photo)
    private void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //TODO: 0- right now maybe just send it directly but maybe later have an upload or cancel option
            //TODO: 1 - use tape to send image to a server / or take a look at android JobScheduler
            //2 - setup a small remote server
            try {
                //File imageFile = new File(imageBitmap);
                mQueue.add(new PhotoUploadTask(createImageFile(imageBitmap)));
            }
            catch(IOException e){
                //
            }
        }
    }

    private File createImageFile(Bitmap bmp) throws IOException {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/findforme";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        File file = new File(dir, "findforme" + tsLong + ".png");
        FileOutputStream fOut = new FileOutputStream(file);

        bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        fOut.flush();
        fOut.close();
        return file;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.findforme, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}