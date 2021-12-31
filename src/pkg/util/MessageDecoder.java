package pkg.util;

import com.google.gson.Gson;
import pkg.vo.MessageVo;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<MessageVo> {
    private static Gson gson = new Gson();

    @Override
    public MessageVo decode(String s) throws DecodeException {
        return gson.fromJson(s, MessageVo.class);
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}
}
