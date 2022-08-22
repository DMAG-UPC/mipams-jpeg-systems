package org.mipams.jumbf.privacy_security.entities.replacement;

import java.io.InputStream;
import java.io.OutputStream;

import org.mipams.jumbf.core.util.CoreUtils;
import org.mipams.jumbf.core.util.MipamsException;

public class RoiParamHandler implements ParamHandlerInterface {

    private int offsetX;
    private int offsetY;

    @Override
    public void writeParamToBytes(OutputStream outputStream) throws MipamsException {
        CoreUtils.writeIntToOutputStream(getOffsetX(), outputStream);
        CoreUtils.writeIntToOutputStream(getOffsetY(), outputStream);
    }

    @Override
    public void populateParamFromBytes(InputStream inputStream) throws MipamsException {

        int offsetX = CoreUtils.readIntFromInputStream(inputStream);
        int offsetY = CoreUtils.readIntFromInputStream(inputStream);

        setOffsetX(offsetX);
        setOffsetY(offsetY);
    }

    @Override
    public long getParamSize() throws MipamsException {
        return 2 * CoreUtils.INT_BYTE_SIZE;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    @Override
    public String toString() {

        final int offsetX = getOffsetX();
        final int offsetY = getOffsetY();

        return "RoiParamHandler(offsetX=" + offsetX + ", label=" + offsetY + ")";
    }
}
