package pkg.util;

import com.google.gson.Gson;
import pkg.vo.MessageVo;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<MessageVo> {
    private static Gson gson = new Gson();

    @Override
    public String encode(MessageVo messageVo) throws EncodeException {
        return gson.toJson(messageVo);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // custom initialization logic
    }

    @Override
    public void destroy() {
        // close resources
    }
}
