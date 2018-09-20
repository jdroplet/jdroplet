package jdroplet.core;

import com.google.gson.reflect.TypeToken;
import jdroplet.bll.Metas;
import jdroplet.data.model.User;
import jdroplet.security.DigestUtil;
import jdroplet.security.FormsAuthenticationTicket;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2017/9/16.
 */
public class SessionFacade {

    private Integer userId;

    public SessionFacade(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void update(String token, FormsAuthenticationTicket ticket) {
        String verifier = DigestUtil.MD5(token);

        updateTickets(verifier, ticket);
    }

    private void updateTickets(String verifie, FormsAuthenticationTicket ticket) {
        Map<String, FormsAuthenticationTicket> tickets = null;

        tickets = (Map<String, FormsAuthenticationTicket>)Metas.getValue(User.class, userId, "session_tokens",
                new TypeToken<HashMap<String, FormsAuthenticationTicket>>() {}.getType());
        if (ticket == null) {
            tickets.put(verifie, ticket);
        } else {
            tickets.remove(verifie);
        }
        Metas.save(User.class, userId, "session_tokens", tickets);
    }

}
