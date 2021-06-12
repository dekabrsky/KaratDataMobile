package com.example.karatdatamobile.main;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karatdatamobile.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeviceListAdapter extends ArrayAdapter<String> {

    private Context context;
    private TextView itemListText;
    private ImageButton onBtn, offBtn;
    private ArrayList<Map.Entry<String, UsbDevice>> listValues;
    private UsbManager mUsbManager;
    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action))
            {
                synchronized (this)
                {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                    {
                        if(device != null)
                        {
                            //Вызовите метод для установки обмена с устройством USB.
                        }
                    }
                    else
                    {
                        Log.d("USB", "permission denied for device " + device);
                    }
                }
            }
        }
    };


    public DeviceListAdapter(Context context, int resource, HashMap<String,
            UsbDevice> devices, UsbManager mUsbManager) {
            super(context, resource, new ArrayList<>(devices.keySet()));
            this.context = context;
            listValues = new ArrayList<>();
            listValues.addAll(devices.entrySet());
            this.mUsbManager = mUsbManager;
    }

    /**
     * getView method is called for each item of ListView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            Map.Entry<String, UsbDevice> currentValue = listValues.get(position);
            UsbDevice device = currentValue.getValue();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.device_list_item, null);

            itemListText = (TextView)convertView.findViewById(R.id.DevInList);
            itemListText.setText(currentValue.getKey() + " " + device.getProductName());

            itemListText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Toast.makeText(context,"Text Working",Toast.LENGTH_SHORT).show();
                        }
            });

            onBtn = (ImageButton)convertView.findViewById(R.id.ButtonDeviceOn);
            //To lazy to implement interface

        onBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mUsbManager.hasPermission(device)) {
                        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context,
                                0, new Intent(ACTION_USB_PERMISSION), 0);
                        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                        context.registerReceiver(mUsbReceiver, filter);
                        mUsbManager.requestPermission(device, mPermissionIntent);
                    }
                    Toast.makeText(context,"Подключен: " +
                            device.getProductName(),Toast.LENGTH_SHORT).show();
                    itemListText.setTypeface(null, Typeface.BOLD);
                }
            });

            offBtn = (ImageButton)convertView.findViewById(R.id.ButtonDeviceOff);
            //To lazy to implement interface
            offBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Я пока не знаю, как и что закрывать",Toast.LENGTH_SHORT).show();
                    itemListText.setTypeface(null, Typeface.NORMAL);
                }
            });

            return convertView;
    }
}