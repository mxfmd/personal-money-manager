package me.dolia.pmm.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Maksym Dolia
 * @since 02.12.2015.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityIT {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  @Autowired
  private WebApplicationContext wac;
  @Autowired
  private FilterChainProxy filterChainProxy;
  private MockMvc mockMvc;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(filterChainProxy).build();

  }

  @Test
  public void testShouldAllowAccessToAppForUsers() throws Exception {
    mockMvc.perform(get("/app").with(user("test")))
        .andExpect(status().isOk())
        .andExpect(authenticated());
  }

  @Ignore
  @Test
  public void testShouldAllowAccessToInnersOfAppForUsers() throws Exception {
    mockMvc.perform(get("/app/accounts").with(user("admin@admin")))
        .andExpect(status().isOk())
        .andExpect(authenticated());
  }

  @Ignore
  @Test
  public void testValidCSRF() throws Exception {
    mockMvc.perform(post("/signin").with(csrf()))
        .andExpect(authenticated());
  }

  @Test
  public void testInvalidCSRF() throws Exception {
    mockMvc.perform(post("/signin").with(csrf().useInvalidToken()))
        .andExpect(unauthenticated());
  }
}
