package coms.block.ui.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import coms.block.ui.model.MediatorRequestBody;
import coms.block.ui.service.HelperService;
import coms.block.ui.model.UserDto;

@RestController
public class ServletController {
	@Value("${coms.service.uri}")
	private String serviceUri;
	@Value("${coms.userservice.uri}")
	private String userserviceUri;
	@Value("${coms.bpmservice.uri}")
	private String bpmserviceUri;
	@Autowired
	HelperService helper;
	@RequestMapping("/hello")
	public String index() {
		return new Date() + "\n";
	}
	@RequestMapping("/hellouser")
	public String getUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    String currentUserName = authentication.getName();
		    return "Hello " + currentUserName;
		}
		return "You are not authorized";
	}
	@RequestMapping(value = "/getuser", produces ="application/json") 
	public UserDto GetUserDetails()
	{
		UserDto userdto = new UserDto();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    String currentUserName = authentication.getName();
		    userdto = helper.GetUserByLoginid(currentUserName);
		    userdto.setUser_password("");
		}
		return userdto;
	}
	
	@RequestMapping("/helloadmin")
	public String getAdmin()
	{
		return "Hello Admin";
	}

	 @PostMapping(value = "/invoke", consumes = "application/json", produces ="application/json") 
	  public String invoke(@RequestBody MediatorRequestBody request) 
	  { 
		  String targetserviceuri = ""; 
		    switch (request.getService()) {
		        case "COMS-API":
		        	targetserviceuri = this.serviceUri; 
		            break;
		        case "COMS-USER-API":
		        	targetserviceuri = this.userserviceUri; 
		            break;
		        case "COMS-BPM-API":
		        	targetserviceuri = this.bpmserviceUri; 
		            break;
		        default:
		        	targetserviceuri = this.serviceUri; 
		            break;
		    }
		  if(targetserviceuri!="")
		  {
			  String response ="";
			  WebClient client = WebClient.create(targetserviceuri); 
			  switch(request.getRequesttype())
			  {
			  	case "get":
			  		response = client.get().uri(request.getOperation()).retrieve().bodyToMono(String.class).block();
			  	break;
			  	case "post":
			  		response = client.post().uri(request.getOperation()).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(request.getRequestbody())).retrieve().bodyToMono(String.class).block();
			  	break;
			  }
			  return response; 
		  }
		  else 
			  return "error";
	  }
}
