package space.arstar.laozhang_wifi01;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import space.arstar.httpwww.laozhang_wifi01.R;

public class CreateImage extends AppCompatActivity {
    private Button createbtn;
    private EditText ssid,passw,shopname;
    private ImageView iv;
    private static int HorL;
    private static  String IMAGEPATH="";
    private Spinner color_spinner;

    private ArrayAdapter<String> colorAdapter;
    //二维码color
    //老张：自定义无信息区域使用颜色，因为imageView对于无信息的像素自我显示为白色，而生成的图片image中
    //无信息含量的像素自动用黑色填充，所以会导致生成的图片全为黑色
    private static final int WHITE= 0xFFFFFFFF;
    private static final int BLACK = 0xff000000;
    private static final int RED= 0xFFFF0000;
    private static final int YELLOW= 0xFDFFE100;
    private static final int GREEN= 0xC000FC00;
    private static final int PURPlE= 0xC0740AFD;
    private static final int PINK= 0xC0FF00AE;
    private static final int SYK= 0xC00B8CFD;
    private static int CHOOSECOLOR=BLACK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_image);
        ssid = (EditText) findViewById(R.id.ssidname);
        passw = (EditText) findViewById(R.id.wifipassword);
        shopname = (EditText) findViewById(R.id.shopname);
        iv = (ImageView) findViewById(R.id.ivcreate);
        createbtn = (Button) findViewById(R.id.create2);
        color_spinner= (Spinner) findViewById(R.id.colorspinner);
        HorL = 0;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //绝对路径可以读取，但是没效果，是不是不用这个，用tostring试试
            IMAGEPATH = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/wifi通灵术";

            File dic = new File(IMAGEPATH);
            if (!dic.exists()) {
                dic.mkdirs();
            }
        }

        //colorlist 初始化
        color_list();

    }



    @Override
    protected void onStart() {
        super.onStart();
        //生成图片
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String info=ssid.getText().toString()+","+passw.getText().toString()+","+shopname.getText().toString();
                if(info!=""){
                    Toast.makeText(CreateImage.this,"请确认输入信息"+info,Toast.LENGTH_SHORT).show();
                    //string[]
//                    String[] arr=info.split(",");
//                    for(int i=0;i<arr.length;i++){
//                        Toast.makeText(CreateImage.this,arr[i],Toast.LENGTH_SHORT).show();
//                    }

                    try {
                        Bitmap qb=EncodingHandler.createQRCode(info,350,CHOOSECOLOR);
                        iv.setImageBitmap(qb);
                        //在这里点击时可以生成图片
//                        FileOutputStream out=new FileOutputStream(IMAGEPATH+"/haha.png");
//                        qb.compress(Bitmap.CompressFormat.PNG,100,out);
//                        out.flush();
//                        out.close();
                        new AlertDialog.Builder(CreateImage.this).setTitle("是否生成图片").setSingleChoiceItems(new String[]{"普通", "高清"}, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        HorL=1;break;
                                    case 1:
                                        HorL=2;break;
                                }
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(CreateImage.this, "其实这里监听不到单选框的哈哈", Toast.LENGTH_SHORT).show();
                                switch (HorL) {
                                    case 1://生成普通图片
                                        new AlertDialog.Builder(CreateImage.this).setTitle("是否选择普通？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Toast.makeText(CreateImage.this, "这时才生成普通", Toast.LENGTH_SHORT).show();
                                                CreateImage.this.createBM(info,1);

                                            }
                                        }).setNegativeButton("取消",null).show();
                                        //Toast.makeText(CreateImage.this, "这TM该显示普通了吧", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2://生成高清图片
                                        new AlertDialog.Builder(CreateImage.this).setTitle("是否选择高清？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                               // Toast.makeText(CreateImage.this, "这时才生成高清", Toast.LENGTH_SHORT).show();
                                                CreateImage.this.createBM(info,2);
                                               // HorL=0;//静态归位
                                            }
                                        }).setNegativeButton("取消",null).show();
                                        //Toast.makeText(CreateImage.this, "我去", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }).setNegativeButton("取消",null).show();

                    } catch (WriterException e) {
                        e.printStackTrace();
                   }
// catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }
//)))))>>>>下面说明枚举而已，与我们设置的int没直接关系
    //声明图片类型
//    public enum BmType{
//        BM_NORMAL,
//        BM_HIGH,
//    }
    //生成图片文件
//    public void createBM(BmType type){
//        switch (type){
//            case BM_NORMAL:
//                break;
//        }
//    }

    //生成图片文件
    public void createBM(String infomation,int number){

        String impath=IMAGEPATH+"/"+shopname.getText().toString()+".png";
//        File file=new File(impath);
//        if(!file.exists()){
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        switch (number){
            case 1:
                try {
                    Bitmap bitmap=EncodingHandler.createQRCode(infomation,500,CHOOSECOLOR);
                    //FileOutputStream outputStream=new FileOutputStream(IMAGEPATH+FileName);
                    FileOutputStream outputStream=new FileOutputStream(impath);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                    outputStream.flush();
                    outputStream.close();
                    HorL=0;//静态归位
                    Toast.makeText(CreateImage.this, "生成普通图片成功，位置为：内存卡-->>wifi通灵术", Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Bitmap bitmap=EncodingHandler.createQRCode(infomation,2000,CHOOSECOLOR);
                    FileOutputStream outputStream=new FileOutputStream(impath);

                    bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                    outputStream.flush();
                    outputStream.close();
                    HorL=0;//静态归位
                    Toast.makeText(CreateImage.this, "生成高清图片成功,位置为：内存卡-->>wifi通灵术", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;


        }
    }

    //选择生成颜色的list
    public void color_list(){
        //spinner使用5步曲
        //一步：添加list
         List<String> colorlist = new ArrayList<String>();
        colorlist.add("主流黑");
        colorlist.add("热血红");
        colorlist.add("至尊黄");
        colorlist.add("天然绿");
        colorlist.add("女神紫");
        colorlist.add("可爱粉");
        colorlist.add("天空蓝");
        //二步：指定适配器
        colorAdapter= new ArrayAdapter<String>(CreateImage.this,R.layout.support_simple_spinner_dropdown_item,colorlist);
        //三步：设置适配器样式 此处设置为下拉式的
        colorAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //四步：添加适配器
        color_spinner.setAdapter(colorAdapter);
        //五步：为下拉菜单设置菜单监听
        color_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:CHOOSECOLOR=BLACK;break;
                    case 1:CHOOSECOLOR=RED;break;
                    case 2:CHOOSECOLOR=YELLOW;break;
                    case 3:CHOOSECOLOR=GREEN;break;
                    case 4:CHOOSECOLOR=PURPlE;break;
                    case 5:CHOOSECOLOR=PINK;break;
                    case 6:CHOOSECOLOR=SYK;break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}

