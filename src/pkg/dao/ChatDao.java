package pkg.dao;

import pkg.util.DBConnection;
import pkg.vo.MessageVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatDao {
    private Connection con;
    private PreparedStatement pst = null;
    private ResultSet res = null;
    private String              sql = null;

    public ChatDao() throws SQLException, ClassNotFoundException {
        con = new DBConnection().getConnection();
    }

    // GET CHAT_LIST ---------------------------------------------
    public ArrayList<MessageVo> getChatList(String currNickname) {
        sql = "SELECT * FROM chats WHERE c_to IS NULL OR c_to = ? OR c_from = ? ORDER BY c_date ASC";
        try {
            ArrayList<MessageVo> list = new ArrayList<>();
            pst = con.prepareStatement(sql);
            pst.setString(1, currNickname);
            pst.setString(2, currNickname);

            res = pst.executeQuery();
            while (res.next()) {
                list.add(new MessageVo(res.getString("c_from"),
                        res.getString("c_to"), res.getString("c_content")));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // INSERT CHATTING -------------------------------------------
    public boolean insertChat(MessageVo messageVo) {
        sql = "INSERT INTO chats(c_from, c_to, c_content) VALUES (?, ?, ?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, messageVo.getFrom());
            pst.setString(2, messageVo.getTo());
            pst.setString(3, messageVo.getContent());

            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
