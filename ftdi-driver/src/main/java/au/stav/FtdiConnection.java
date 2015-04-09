package au.stav;

import com.sun.jna.Memory;
import com.sun.jna.ptr.IntByReference;

public class FtdiConnection implements AutoCloseable {

    static {
        System.setProperty("java.library.path", "/usr/local/lib");
    }
    
    private D2XX ftd2xx = D2XX.INSTANCE;
    private int deviceHandle;
    
    public FtdiConnection() {
        IntByReference devNum = new IntByReference();
        ensureFTStatus(ftd2xx.FT_CreateDeviceInfoList(devNum));
        
        IntByReference flag = new IntByReference();
        IntByReference devType = new IntByReference();
        IntByReference devID = new IntByReference();
        IntByReference locID = new IntByReference();
        IntByReference ftHandle = new IntByReference();
        Memory devSerNum = new Memory(16);
        Memory devDesc = new Memory(64);

        ensureFTStatus(ftd2xx.FT_GetDeviceInfoDetail(0, flag, devType, devID, locID, devSerNum, devDesc, ftHandle));
        
        IntByReference handle = new IntByReference();
        ensureFTStatus(ftd2xx.FT_OpenEx(devSerNum, 1, handle));
        this.deviceHandle = handle.getValue();
        
        ensureFTStatus(ftd2xx.FT_SetBaudRate(deviceHandle, 9600));
        ensureFTStatus(ftd2xx.FT_SetDataCharacteristics(deviceHandle, (byte)8, (byte)0, (byte)0));
        ensureFTStatus(ftd2xx.FT_SetTimeouts(deviceHandle, 1000, 1000));
        ensureFTStatus(ftd2xx.FT_SetBitMode(deviceHandle, (byte)0xFF, (byte)0x04));
        
        System.out.println("FTDI device connected");
    }

    private static void ensureFTStatus(int ftstatus) {
        if (ftstatus != 0) {
            throw new FtdiException(ftstatus);
        }
    }
    
    public boolean write(byte b) {
        Memory data = new Memory(1);
        data.write(0, new byte[]{b}, 0, 1);
        IntByReference wrote = new IntByReference();
        ensureFTStatus(ftd2xx.FT_Write(deviceHandle, data, 1, wrote));
        return (wrote.getValue() == 1);
    }

    @Override
    public void close() {
        ensureFTStatus(ftd2xx.FT_Close(deviceHandle));
    }

}
