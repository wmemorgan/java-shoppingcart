package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.ShoppingcartApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingcartApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void A_findUserById() {
        System.out.println("Expected: cinnamon");
        System.out.println("Actual: " + userService.findUserById(2).getUsername());
        assertEquals("cinnamon", userService.findUserById(2).getUsername());
    }

    @Test
    public void B_findByNameContaining() {
        System.out.println("Expected: 2");
        System.out.println("Actual: " + userService.findByNameContaining("n").size());
        assertEquals(2, userService.findByNameContaining("n").size());
    }
}