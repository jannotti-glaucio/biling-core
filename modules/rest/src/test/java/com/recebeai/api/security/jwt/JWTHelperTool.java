package tech.jannotti.billing.api.security.jwt;

import tech.jannotti.billing.core.persistence.model.base.BaseRole;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.rest.security.util.JWTHelper;

public class JWTHelperTool {

    public static void main(String args[]) {

        BaseUser user = new BaseUser();
        user.setUsername("teste");
        user.setEmail("teste@jannotti.tech");
        user.setRealName("Teste");
        user.setPassword("abc123");

        BaseRole role = new BaseRole();
        role.setCode("R");
        role.setName("Role");
        user.setRole(role);

        String token = JWTHelper.buildToken(user, "abc123", 1);
        System.out.println(token);
    }

}
