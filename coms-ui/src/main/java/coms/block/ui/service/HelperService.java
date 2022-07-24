package coms.block.ui.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import coms.block.ui.model.UserDto;
@Component
public class HelperService {
	@Value("${coms.userservice.uri}")
	private String userserviceUri;
	 public UserDto GetUserByLoginid(String loginid)
	 {
		 WebClient client = WebClient.create(this.userserviceUri); 

		  UserDto userdto = client.get()
				  .uri(uriBuilder -> uriBuilder
					.path("/getuserbyloginid/{loginid}")
					.build(loginid))
				  .retrieve()
				  .bodyToMono(UserDto.class).block();
		  return userdto;
	 }
}
