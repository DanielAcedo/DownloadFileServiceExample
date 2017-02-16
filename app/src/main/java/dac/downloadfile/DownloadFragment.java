package dac.downloadfile;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by usuario on 16/02/17.
 */

public class DownloadFragment extends Fragment {

    private Button btn_click;
    private static final int REQUEST_WRITE_EXTERNAL = 287;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_download, container, false);

        btn_click = (Button)root.findViewById(R.id.btn_click);
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkWritePermission()){
                    onDownload();
                }
            }
        });

        return root;
    }

    private boolean checkWritePermission(){
        final String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), permission);

        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)){
            Snackbar.make(getView(), R.string.permission, Snackbar.LENGTH_LONG).setAction(android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermissions(new String[]{permission}, REQUEST_WRITE_EXTERNAL);
                }
            }).show();
            return false;
        }
        else{
            requestPermissions(new String[]{permission}, REQUEST_WRITE_EXTERNAL);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        Log.d("Prueba", "Ha entrado en permisos");
        if(requestCode == REQUEST_WRITE_EXTERNAL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                onDownload();
            }
        }
    }

    private void onDownload(){
        btn_click.setEnabled(false);
        Intent intent = new Intent(getActivity(), Downloader.class);
        intent.setData(Uri.parse("https://commonsware.com/Android/Android-1_0-CC.pdf"));
        getActivity().startService(intent);
    }
}
