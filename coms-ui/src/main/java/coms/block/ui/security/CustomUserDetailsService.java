package coms.block.ui.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import coms.block.ui.service.HelperService;
import coms.block.ui.model.UserDto;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Value("${coms.userservice.uri}")
	private String userserviceUri;
	@Autowired
	HelperService helper;
	@Override
	/*public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<SimpleGrantedAuthority> roles = null;
		if(username.equals("admin@coms.com"))
		{
			roles=Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
			return new User("admin@coms.com","$2a$10$0Nv/Qa7m8DdjWpBS2XRZWeP8rWDB7OdScb2grQSRDS9I9fWWlBNG2",roles);
		}
		if(username.equals("user@coms.com"))
		{
			roles=Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
			return new User("user@coms.com","$2a$10$5VikX1NNQFL9f.N7Ta5wVuBL5HuPi7ro5Q3UZYGVOCURwiotGrVCS",roles);
		}
		throw new UsernameNotFoundException("User not found with the name "+ username);
	}*/
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
		UserDto userdto = helper.GetUserByLoginid(username);
		/*WebClient client = WebClient.create(this.userserviceUri); 

		  UserDto userdto = client.get()
				  .uri(uriBuilder -> uriBuilder
					.path("/getuserbyloginid/{loginid}")
					.build(username))
				  .retrieve()
				  .bodyToMono(UserDto.class).block();*/
		  //userdto.getRoles().forEach((n) -> roles.add(new SimpleGrantedAuthority(n)));
		  roles=Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
		   
		  if(userdto!=null)
		  {
			  System.out.println("password retrieved--"+ userdto.getUser_password());
			  return new User(userdto.getLogin_id(),userdto.getUser_password(),roles);
		  }
		  else
			  throw new UsernameNotFoundException("User not found with the name "+ username);
		/*if(username.equals("admin@coms.com"))
		{
			roles=Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
			return new User("admin@coms.com","$2a$10$0Nv/Qa7m8DdjWpBS2XRZWeP8rWDB7OdScb2grQSRDS9I9fWWlBNG2",roles);
		}
		if(username.equals("user@coms.com"))
		{
			roles=Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
			return new User("user@coms.com","$2a$10$5VikX1NNQFL9f.N7Ta5wVuBL5HuPi7ro5Q3UZYGVOCURwiotGrVCS",roles);
		}*/
	
	}

}
