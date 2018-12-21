package com.codeoftheweb.salvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
}
	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository,
									  GameRepository gameRepository, GamePlayerRepository gameplayerrepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {
			// save a couple of customers
			Player p1=new Player("Bauer@gmail.com","24");
			playerRepository.save(p1);
			Player p2=new Player("Brian@gmail.com", "42");
			playerRepository.save(p2);
			Player p3=new Player("JackBauer@gmail.com", "kb");
			playerRepository.save(p3);
			Player p4=new Player("Palmer@gmail.com", "mole");
			playerRepository.save(p4);
			Player p5=new Player("Dessler@gmail.com", "hello");
			playerRepository.save(p5);
			Player p6=new Player("soni.ranu8@gmail.com", "hii");
			playerRepository.save(p6);

			Game g1=new Game("fusball");
			gameRepository.save(g1);
			Game g2=new Game();
			gameRepository.save(g2);
			Game g3=new Game();
			gameRepository.save(g3);
			Game g4=new Game();
			gameRepository.save(g4);
			Game g5=new Game();
			gameRepository.save(g5);
			Game g6=new Game();
			gameRepository.save(g6);

			GamePlayer gp1=new GamePlayer(g1,p1);
			gameplayerrepository.save(gp1);
			GamePlayer gp2=new GamePlayer(g1, p2);
			gameplayerrepository.save(gp2);
			GamePlayer gp3=new GamePlayer(g2, p1);
			gameplayerrepository.save(gp3);
			GamePlayer gp4=new GamePlayer(g2, p2);
			gameplayerrepository.save(gp4);
			GamePlayer gp5=new GamePlayer(g3, p3);
			gameplayerrepository.save(gp5);
			GamePlayer gp6=new GamePlayer(g3, p4);
			gameplayerrepository.save(gp6);
			GamePlayer gp7=new GamePlayer(g4, p3);
			gameplayerrepository.save(gp7);
			GamePlayer gp8=new GamePlayer(g4, p4);
			gameplayerrepository.save(gp8);
			GamePlayer gp9=new GamePlayer(g5, p1);
			gameplayerrepository.save(gp9);



			List<String>Loc1 = Arrays.asList("H2","H3","H4");
			Ship Destroyer = new Ship("Destroyer",Loc1,gp1);
			shipRepository.save(Destroyer);
			List<String>Loc2 = Arrays.asList("E1","F1","G1");
			Ship Submarine = new Ship("Submarine",Loc2,gp1);
			shipRepository.save(Submarine);
			List<String>Loc55 = Arrays.asList("A1","A2","A3","A4", "A5");
			Ship PatrolBoat = new Ship("PatrolBoat",Loc55,gp1);
			shipRepository.save(PatrolBoat);
			List<String>Loc56 = Arrays.asList("B1","B2");
			Ship Fishingboat = new Ship("Fishingboat",Loc56,gp1);
			shipRepository.save(Fishingboat);
			List<String>Loc57 = Arrays.asList("C1","C2","C3");
			Ship Speedboat = new Ship("Speedboat",Loc57,gp1);
			shipRepository.save(Speedboat);
			List<String>Loc3 = Arrays.asList("B4", "B5");
			Ship PatrolBoat2 =new Ship("PatrolBoat",Loc3,gp2);
			shipRepository.save(PatrolBoat2);


			List<String>Salvo1 = Arrays.asList("B4", "B5", "B6");
			Salvo s1 =new Salvo(1,Salvo1,gp1, p1.getId());
			salvoRepository.save(s1);
			List<String>Salvo2 = Arrays.asList("B5", "C5", "F1");
			Salvo s2 =new Salvo(1,Salvo2,gp2,p2.getId());
			salvoRepository.save(s2);
			List<String>Salvo3 = Arrays.asList("F2", "D5");
			Salvo s3 =new Salvo(2,Salvo3,gp1,p1.getId());
			salvoRepository.save(s3);
			List<String>Salvo4 = Arrays.asList("E1", "H3", "A2");
			Salvo s4 =new Salvo(2,Salvo4,gp2,p2.getId());
			salvoRepository.save(s4);
            List<String>Salvo5 = Arrays.asList("A2", "A4", "G6");
            Salvo s5 =new Salvo(1,Salvo5,gp3,p3.getId());
            salvoRepository.save(s5);
            List<String>Salvo6 = Arrays.asList("B5", "D5", "C7");
            Salvo s6 =new Salvo(1,Salvo6,gp4,p4.getId());
            salvoRepository.save(s6);
			List<String>Salvo7 = Arrays.asList("A3", "H6");
			Salvo s7 =new Salvo(2,Salvo7,gp3,p3.getId());
			salvoRepository.save(s7);
			List<String>Salvo8 = Arrays.asList("C5", "C6");
			Salvo s8 =new Salvo(2,Salvo8,gp4,p4.getId());
			salvoRepository.save(s8);
            List<String>Salvo9 = Arrays.asList("G6", "H6", "A4");
            Salvo s9 =new Salvo(1,Salvo9,gp5,p5.getId());
            salvoRepository.save(s9);
            List<String>Salvo11 = Arrays.asList("H1", "H2", "H3");
            Salvo s11 =new Salvo(1,Salvo11,gp6,p6.getId());
            salvoRepository.save(s11);
			List<String>Salvo10 = Arrays.asList("A2", "A3", "D8");
			Salvo s10 =new Salvo(2,Salvo10,gp6,p6.getId());
			salvoRepository.save(s10);
			List<String>Salvo12 = Arrays.asList("E1", "F2", "G3");
			Salvo s12 =new Salvo(2,Salvo12,gp6,p6.getId());
			salvoRepository.save(s12);
			List<String>Salvo13 = Arrays.asList("A3", "A4", "F7");
			Salvo s13 =new Salvo(1,Salvo13,gp6,p6.getId());
			salvoRepository.save(s13);
			List<String>Salvo14 = Arrays.asList("B5", "C6", "H1");
			Salvo s14 =new Salvo(1,Salvo14,gp6,p6.getId());
			salvoRepository.save(s14);



			Score scr1=new Score(g1, p1, 1.0);
			scoreRepository.save(scr1);

			Score scr2=new Score(g1, p2, 0.0);
			scoreRepository.save(scr2);

			Score scr3=new Score(g2, p1, 0.5);
			scoreRepository.save(scr3);

			Score scr4=new Score(g2, p2, 0.5);
			scoreRepository.save(scr4);

			Score scr5=new Score(g3, p3, 1.0);
			scoreRepository.save(scr5);

			Score scr6=new Score(g3, p4, 0.0);
			scoreRepository.save(scr6);

			Score scr7=new Score(g4, p3, 1.0);
			scoreRepository.save(scr7);

			Score scr8=new Score(g4, p4, 0.0);
			scoreRepository.save(scr8);


		};
	}
}

	@Configuration
	class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
		PlayerRepository playerRepository;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(inputName -> {
				Player player = playerRepository.findByUserName(inputName);
				if (player != null) {
					return new User(player.getUserName(), player.getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("Unknown user: " + inputName);
				}
			});
		}
	}


@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()

				.antMatchers("/**").permitAll()
				.antMatchers("/api/logout").hasAuthority("USER")
				.antMatchers("/rest/**").denyAll()
				.and()
				.formLogin();

		http.formLogin()
				.usernameParameter("userName")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");
		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}








	//@Configuration
//@EnableWebSecurity
//class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//				.authorizeRequests()
//				.antMatchers("/**").permitAll()
//				.anyRequest().authenticated()
//				.and()
//				.formLogin()
//				.usernameParameter("userName")
//				.passwordParameter("password")
//				.loginPage("/api/login")
//				.permitAll()
//				.and()
//				.logout()
//				.permitAll();
//	}
//}




