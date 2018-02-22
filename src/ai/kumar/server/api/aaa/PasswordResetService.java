
package ai.kumar.server.api.aaa;

import java.util.regex.Pattern;

import org.json.JSONObject;

import ai.kumar.DAO;
import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.APIException;
import ai.kumar.server.APIHandler;
import ai.kumar.server.AbstractAPIHandler;
import ai.kumar.server.Authentication;
import ai.kumar.server.Authorization;
import ai.kumar.server.BaseUserRole;
import ai.kumar.server.ClientCredential;
import ai.kumar.server.Query;
import ai.kumar.server.ServiceResponse;
import ai.kumar.tools.TimeoutMatcher;

import javax.servlet.http.HttpServletResponse;

public class PasswordResetService extends AbstractAPIHandler implements APIHandler {

	private static final long serialVersionUID = -8893457607971788891L;

	@Override
	public String getAPIPath() {
		return "/aaa/resetpassword.json";
	}

	@Override
	public BaseUserRole getMinimalBaseUserRole() {
		return BaseUserRole.ANONYMOUS;
	}

	@Override
	public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
		return null;
	}

	@Override
	public ServiceResponse serviceImpl(Query call, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions)
			throws APIException {
		JSONObject result = new JSONObject(true);
		result.put("accepted", false);
		result.put("message", "Error: Unable to reset Password");

		String newpass = call.get("newpass", null);

		ClientCredential credential = new ClientCredential(ClientCredential.Type.resetpass_token,
				call.get("token", null));
		Authentication authentication = new Authentication(credential, DAO.passwordreset);
		ClientCredential emailcred = new ClientCredential(ClientCredential.Type.passwd_login,
				authentication.getIdentity().getName());

		String passwordPattern = DAO.getConfig("users.password.regex", "^(?=.*\\d).{6,64}$");

		Pattern pattern = Pattern.compile(passwordPattern);

		if ((authentication.getIdentity().getName()).equals(newpass) || !new TimeoutMatcher(pattern.matcher(newpass)).matches()) {
			// password can't equal email and regex should match
			throw new APIException(400, "invalid password");
		}

		if (DAO.hasAuthentication(emailcred)) {
			Authentication emailauth = DAO.getAuthentication(emailcred);
			String salt = createRandomString(20);
			emailauth.remove("salt");
			emailauth.remove("passwordHash");
			emailauth.put("salt", salt);
			emailauth.put("passwordHash", getHash(newpass, salt));
		}

		if (authentication.has("one_time") && authentication.getBoolean("one_time")) {
			authentication.delete();
		}
		result.put("accepted", true);
		result.put("message", "Your password has been changed!");
		return new ServiceResponse(result);
	}

}
