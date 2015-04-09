package au.stav;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public interface D2XX extends Library {
    D2XX INSTANCE = (D2XX) Native.loadLibrary("ftd2xx", D2XX.class);
    
    /**
     * This function builds a device information list and returns the number of 
     * D2XX devices connected to the system.  The list contains information 
     * about both unopen and open devices.
     * @param lpdwNumDevs Pointer to unsigned long(long) to store the number of 
     * devices connected.
     * @return FT_STATUS: FT_OK if successful, otherwise the return value is an 
     * FT error code.
     */
    int FT_CreateDeviceInfoList(IntByReference lpdwNumDevs);
    
    /**
     * This function returns an entry from the device information list.
     * @param dwIndex Index of the entry in the device info list.
     * @param lpdwFlags Pointer to unsigned long to store the flag value.
     * @param lpdwType Pointer to unsigned long to store device type.
     * @param lpdwID Pointer to unsigned long to store device ID.
     * @param lpdwLocId Pointer to unsigned long to store the device location ID.
     * @param pcSerialNumber Pointer to buffer to store device serial number as 
     * a nullterminated string.
     * @param pcDescription Pointer to buffer to store device description as a 
     * null-terminated string.
     * @param ftHandle  Pointer to a variable of type FT_HANDLE where the handle
     * will be stored.
     * @return FT_STATUS: FT_OK if successful, otherwise the return value is an 
     * FT error code.
     */
    int FT_GetDeviceInfoDetail(int dwIndex, IntByReference lpdwFlags,
            IntByReference lpdwType, IntByReference lpdwID,
            IntByReference lpdwLocId, Pointer pcSerialNumber,
            Pointer pcDescription, IntByReference ftHandle);

    /**
     * Open the specified device and return a handle that will be used for
     * subsequent accesses.  The device can be specified by its serial number,
     * device description or location. This function can also be used to open
     * multiple devices simultaneously.  Multiple devices can be specified by 
     * serial number, device description or location ID (location information 
     * derived from the physical location of a device on USB).  Location IDs for
     * specific USB ports can be obtained using the utility USBView and are 
     * given in hexadecimal format.  Location IDs for devices connected to a 
     * system can be obtained by calling FT_GetDeviceInfoList or FT_ListDevices 
     * with the appropriate flags. 
     * @param pvArg1 Pointer to an argument whose type depends on the value of 
     * dwFlags.  It is normally be interpreted as a pointer to a null 
     * terminated string. 
     * @param dwFlags FT_OPEN_BY_SERIAL_NUMBER, FT_OPEN_BY_DESCRIPTION or 
     * FT_OPEN_BY_LOCATION.
     * @param ftHandle Pointer to a variable of type FT_HANDLE where the handle 
     * will be stored.  This handle must be used to access the device. 
     * @return FT_STATUS: FT_OK if successful, otherwise the return value is an 
     * FT error code.
     */
    int FT_OpenEx(Pointer pvArg1, int dwFlags, IntByReference ftHandle);
    
    /**
     * This function sets the baud rate for the device. 
     * @param ftHandle Handle of the device.
     * @param dwBaudRate Baud rate. 
     * @return FT_STATUS: FT_OK if successful, otherwise the return value is an 
     * FT error code.
     */
    int FT_SetBaudRate(int ftHandle, int dwBaudRate);
    
    /**
     * This function sets the data characteristics for the device. 
     * @param ftHandle Handle of the device. 
     * @param uWordLength Number of bits per word - must be FT_BITS_8 or 
     * FT_BITS_7. 
     * @param uStopBits Number of stop bits - must be FT_STOP_BITS_1 or
     * FT_STOP_BITS_2. 
     * @param uParity Parity - must be FT_PARITY_NONE, FT_PARITY_ODD, 
     * FT_PARITY_EVEN, FT_PARITY_MARK or FT_PARITY SPACE. 
     * @return FT_STATUS: FT_OK if successful, otherwise the return value is an 
     * FT error code.
     */
    int FT_SetDataCharacteristics(int ftHandle, byte uWordLength, byte uStopBits, byte uParity);
    
    /**
     * This function sets the read and write timeouts for the device. 
     * @param ftHandle Handle of the device.
     * @param dwReadTimeout Read timeout in milliseconds.
     * @param dwWriteTimeout Write timeout in milliseconds.
     * @return FT_STATUS: FT_OK if successful, otherwise the return value is an 
     * FT error code.
     */
    int FT_SetTimeouts(int ftHandle, int dwReadTimeout, int dwWriteTimeout);
    
    /**
     * Enables different chip modes. 
     * @param ftHandle Handle of the device. 
     * @param ucMask Required value for bit mode mask.  This sets up which bits
     * are  inputs and outputs.  A bit value of 0 sets the corresponding pin to
     * an input, a bit value of 1 sets the corresponding pin to an output. 
     * In the case of CBUS Bit Bang, the upper nibble of this value controls 
     * which pins are inputs and outputs, while the lower nibble controls which
     * of the outputs are high and low. 
     * @param ucMode Mode value.  Can be one of the following: 
     * 0x0 = Reset 
     * 0x1 = Asynchronous Bit Bang 
     * 0x2 = MPSSE (FT2232, FT2232H, FT4232H and FT232H devices only) 
     * 0x4 = Synchronous Bit Bang (FT232R, FT245R, FT2232, FT2232H, FT4232H and 
     * FT232H devices only) 
     * 0x8 = MCU Host Bus Emulation Mode (FT2232, FT2232H, FT4232H and FT232H 
     * devices only) 
     * 0x10 = Fast Opto-Isolated Serial Mode (FT2232, FT2232H, FT4232H and 
     * FT232H devices only) 
     * 0x20 = CBUS Bit Bang Mode (FT232R and FT232H devices only)  
     * 0x40 = Single Channel Synchronous 245 FIFO Mode (FT2232H and FT232H 
     * devices only) 
     * @return FT_STATUS: FT_OK if successful, otherwise the return value is an 
     * FT error code.
     */
    int FT_SetBitMode(int ftHandle, byte ucMask, byte ucMode);
    
    /**
     * Write data to the device. 
     * @param ftHandle Handle of the device.
     * @param lpBuffer Pointer to the buffer that contains the data to be 
     * written to the device. 
     * @param dwBytesToWrite Number of bytes to write to the device. 
     * @param lpdwBytesWritten Pointer to a variable of type DWORD which 
     * receives the number of bytes written to the device. 
     * @return FT_STATUS: FT_OK if successful, otherwise the return value is an 
     * FT error code.
     */
    int FT_Write(int ftHandle, Pointer lpBuffer, int dwBytesToWrite, IntByReference lpdwBytesWritten);
    
    /**
     * Close an open device.
     * @param ftHandle Handle of the device. 
     * @return FT_STATUS: FT_OK if successful, otherwise the return value is an 
     * FT error code.
     */
    int FT_Close(int ftHandle);
}