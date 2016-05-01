package space.arstar.laozhang_wifi01;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import space.arstar.httpwww.laozhang_wifi01.R;

public class MainActivity extends AppCompatActivity {
    private Button conbtn,tonglingbtn;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private WifiConfiguration wifiConfiguration;
    private static String SSID="";
    private static String PASSWORD="";
    private static String SHOPNAME="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conbtn= (Button) findViewById(R.id.connec);
        tonglingbtn= (Button) findViewById(R.id.tongling);
        conbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiInfo=wifiManager.getConnectionInfo();
                if(!wifiManager.isWifiEnabled()){
                    wifiManager.setWifiEnabled(true);
                    Toast.makeText(MainActivity.this,"请授权后请再次扫描",Toast.LENGTH_SHORT).show();
                }
                if(wifiManager.isWifiEnabled()){
                    //进入captureActivity进行扫描
                    Intent gocapture=new Intent(MainActivity.this,CaptureActivity.class);
                    startActivityForResult(gocapture,0);




                }

            }
        });

        tonglingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goCreate=new Intent(MainActivity.this,CreateImage.class);
                startActivity(goCreate);
                //finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(MainActivity_WIFI.this,"怎么回事",Toast.LENGTH_SHORT).show();
        if(requestCode==0&&data!=null){
            Bundle bundle=data.getExtras();
            String meg=bundle.getString("result");
            //  new AlertDialog.Builder(MainActivity_WIFI.this).setTitle(SHOPNAME).setIcon(R.drawable.changle).setMessage("成i").setPositiveButton("确定",null).show();

            // Toast.makeText(MainActivity_WIFI.this,meg,Toast.LENGTH_SHORT).show();
            try {
                String[] ssid_password=meg.split(",");
                SSID=ssid_password[0];
                PASSWORD=ssid_password[1];
                SHOPNAME=ssid_password[2];
                // Toast.makeText(MainActivity_WIFI.this,SSID+"/"+PASSWORD+"/"+SHOPNAME,Toast.LENGTH_SHORT).show();
                wifiConfiguration=new WifiConfiguration();
                wifiConfiguration.SSID="\""+SSID+"\"";
                wifiConfiguration.preSharedKey="\""+PASSWORD+"\"";
                wifiConfiguration.status=WifiConfiguration.Status.ENABLED;
                wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

                int netid=wifiManager.addNetwork(wifiConfiguration);
                boolean isconnect=wifiManager.enableNetwork(netid,true);

                if(isconnect){
                    new AlertDialog.Builder(MainActivity.this).setTitle(SHOPNAME).setIcon(R.drawable.changle).setMessage("成功链接wifi").setPositiveButton("确定",null).show();
                }
            }catch (Exception e){
                Toast.makeText(MainActivity.this,"错误二维码",Toast.LENGTH_SHORT).show();
            }

        }
        if(resultCode!=0){
            Toast.makeText(MainActivity.this,"请检查商家所提供二维码正确性",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SSID=PASSWORD=SHOPNAME="";
    }
}
