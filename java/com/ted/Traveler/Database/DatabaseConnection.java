package com.ted.Traveler.Database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.ted.Traveler.Database.Listing;
import com.ted.Traveler.Database.User;

public class DatabaseConnection {
	
    @SuppressWarnings("unchecked")
	public List<User> getUsers()
    {
        List<User> users = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        
        //Query q = em.createQuery("Select u from User u");
        Query q = entitymanager.createNamedQuery("User.findAll");
        users =  q.getResultList();
		
        tx.commit();
        entitymanager.close();
        return users;
    }
	
    @SuppressWarnings("unchecked")
	public List<Listing> getListings()
    {
        List<Listing> listings = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
        Query q = entitymanager.createNamedQuery("Listing.findAll");
        listings =  q.getResultList();
		
        transaction.commit();
        entitymanager.close();
        return listings;
    }

	public int getListingsNumber()
    {
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
		Query q = entitymanager.createQuery("SELECT COUNT(*) FROM Listing");
        List listingsCount = q.getResultList();
        int count = Integer.parseInt(listingsCount.get(0).toString());
		
        transaction.commit();
        entitymanager.close();
        return count;
    }

	public int getListingNumber(int listingid)
    {
        List<Listing> listings = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Query q = entitymanager.createNamedQuery("Listing.findAll");
        listings =  q.getResultList();
        
        transaction.commit();
        entitymanager.close();
        
        for(int i=0; i<listings.size(); i++)
        	if (listings.get(i).getId() == listingid)
        		return i;
        return 0;
    }
	
    @SuppressWarnings("unchecked")
	public List<Calendar> getBookings()
    {
        List<Calendar> bookings = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();		

        Query query = entitymanager.createQuery("Select c from Calendar c where c.userid > 0");
        bookings = query.getResultList();
        
        transaction.commit();
        entitymanager.close();
        return bookings;
    }

	
    @SuppressWarnings("unchecked")
	public List<History> getUserHistory(int userid)
    {
        List<History> historylist = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
        Query query = entitymanager.createQuery("Select h from History h where h.userId = :userid");
        query.setParameter("userid", userid);
        historylist =  query.getResultList();
		
        transaction.commit();
        entitymanager.close();
        return historylist;
    }

    @SuppressWarnings("unchecked")
	public List<Calendar> getBookListings(int userid)
    {
        List<Calendar> calendarlist = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
        Query query = entitymanager.createQuery("Select c from Calendar c where c.userid = :userid");
        query.setParameter("userid", userid);
        calendarlist =  query.getResultList();
		
        transaction.commit();
        entitymanager.close();
        return calendarlist;
    }
    
    @SuppressWarnings("unchecked")
	public List<Review> getReviews()
    {
        List<Review> reviews = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
        Query q = entitymanager.createNamedQuery("Review.findAll");
        reviews =  q.getResultList();
		
        transaction.commit();
        entitymanager.close();
        return reviews;
    }

    @SuppressWarnings("unchecked")
	public List<Message> getMessages(int roomid, int hostid)
    {
        List<Message> messages = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
        Query query = entitymanager.createQuery("Select m from Message m where m.listingId = :roomid and m.receiverId = :hostid");
        query.setParameter("roomid", roomid);
        query.setParameter("hostid", hostid);
        messages = query.getResultList();
		
        transaction.commit();
        entitymanager.close();
        return messages;
    }
	
    @SuppressWarnings("unchecked")
	public List<Score> getScores()
    {
        List<Score> scores = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
        Query q = entitymanager.createNamedQuery("Score.findAll");
        scores =  q.getResultList();
		
        transaction.commit();
        entitymanager.close();
        return scores;
    }
	
	public List<Score> getScoreListings(int listingid)
    {
        List<Score> scores = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Query query = entitymanager.createQuery("Select s from Score s where s.listingId = :listingid");
        query.setParameter("listingid", listingid);
        scores =  query.getResultList();
		
        transaction.commit();
        entitymanager.close();
        return scores;
    }
	
	public List<Score> getUserScores(int userid)
    {
        List<Score> scores = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
		Query query = entitymanager.createQuery("Select s from Score s where s.reviewerId = :id");
		query.setParameter("id", userid);
		
		scores =  query.getResultList();
		
        transaction.commit();
        entitymanager.close();
        return scores;
    }
	
	public boolean userhasBooked(int userid) {
		List<Score> scores = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
		Query query = entitymanager.createQuery("Select s from Score s where s.reviewerId = :id");
		query.setParameter("id", userid);
		
		scores =  query.getResultList();
		
        transaction.commit();
        entitymanager.close();
        
        return (!scores.isEmpty());
	}

	public int getNumberOfEntries()
    {
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
		Query q = entitymanager.createQuery("SELECT COUNT(*) FROM History");
        List historyCount = q.getResultList();
        int count = Integer.parseInt(historyCount.get(0).toString());
		
        transaction.commit();
        entitymanager.close();
        return count;
    }

	public int getNumberOfMessages()
    {
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
		Query q = entitymanager.createQuery("SELECT COUNT(*) FROM Message");
        List messageCount = q.getResultList();
        int count = Integer.parseInt(messageCount.get(0).toString());
		
        transaction.commit();
        entitymanager.close();
        return count;
    }
	
    public void updateUserHistory(int userid, int listingid) 
    {
        History userhistory = new History();
        userhistory.setId(this.getNumberOfEntries());
        userhistory.setUserId(userid);
        userhistory.setListingId(listingid);
        userhistory.setListingNumber(this.getListingNumber(listingid));
        
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        
    	entitymanager.persist(userhistory);
    	entitymanager.flush();
    	
        tx.commit();
    	entitymanager.close();
    }
       

    public boolean historyExists(int userid, int listingid) 
    {
    	List<History> list = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();

        Query query = entitymanager.createQuery("Select h from History h where h.userId = :userid and h.listingId = :listingid");
        query.setParameter("userid", userid);
        query.setParameter("listingid", listingid);
    	list = query.getResultList();
        
        tx.commit();
    	entitymanager.close();
    	
    	return (!list.isEmpty());
    }
      
    
    public User find(String username, String password)
    {
        User user = null;
        
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        
        Query q = entitymanager.createQuery("Select u from User u where u.username = :username and u.password = :password");
        q.setParameter("username", username);
        q.setParameter("password", password);
        List users =  q.getResultList();
        tx.commit();
        entitymanager.close();
        
        if (users != null && users.size() == 1)
        {
            user = (User) users.get(0);
        }

        return user;
    }

    
    public Listing getListing(int listingid)
    {
        Listing listing = null;
        
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
        listing = entitymanager.find(Listing.class, listingid);
        
        transaction.commit();
        entitymanager.close();
        
        return listing;
    }
    
    public void insertListing(Listing listing)
    {
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        try 
        {
        	entitymanager.persist(listing);
        	entitymanager.flush();
            tx.commit();
        }
        catch (PersistenceException e)
        {
            if (tx.isActive()) tx.rollback();
        }
        finally 
        {
        	entitymanager.close();
        }
    }
    
    public void insertCalendar(Calendar calendar)
    {
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        try 
        {
        	entitymanager.persist(calendar);
        	entitymanager.flush();
            tx.commit();
        }
        catch (PersistenceException e)
        {
            if (tx.isActive()) tx.rollback();
        }
        finally 
        {
        	entitymanager.close();
        }
    }
    
    public int insertUser(User user)
    {
        int id = -1;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        try 
        {
        	entitymanager.persist(user);
        	entitymanager.flush();
            id = user.getUserID();
            tx.commit();
            return id;
        }
        catch (PersistenceException e)
        {
            if (tx.isActive()) tx.rollback();
            return id;
        }
        finally 
        {
        	entitymanager.close();
        }
    }
    
    public User getById(int id)
    {
        User user = null;
        
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        
        user = entitymanager.find(User.class, id);
	
        tx.commit();
        entitymanager.close();
        
        
        return user;
    }
    
    public User loginService(String username, String password) {
		return find(username,password);
    }
    
    public User getUserById(int userid) {		
		User user = null;
		List<User> users = null;
        
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        
        users = (List<User>) entitymanager.createNamedQuery("User.findByUserId").setParameter("id", userid).getResultList();
	
        tx.commit();
        entitymanager.close();
        
        if (users.isEmpty()) return null;
		else user = users.get(0);
		
		return user;
	}
    
    public User getUserByUsername(String username) {		
		User user = null;
		List<User> users = null;
        
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        
        users = (List<User>) entitymanager.createNamedQuery("User.findByUsername").setParameter("username", username).getResultList();
	
        tx.commit();
        entitymanager.close();
        
        if (users.isEmpty()) return null;
		else user = users.get(0);
		
		return user;
	}

	//Calculate the number of the rows and returns the result 
	public int generateCalendarID() {
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        List calendars = entitymanager.createQuery("SELECT COUNT(*) FROM Calendar").getResultList();
        entitymanager.close();
		return (Integer.parseInt(calendars.get(0).toString())+1);
	}

	//Calculate the number of the rows and returns the result 
	public int generateMessageID() {
	    List<Message> messages = null;
	    EntityManager entitymanager = JPAResource.factory.createEntityManager();
	    EntityTransaction transaction = entitymanager.getTransaction();
	    transaction.begin();
	       
		Query query = entitymanager.createQuery("Select m from Message m ORDER BY m.messageId");
		messages =  query.getResultList();
			
	    transaction.commit();
	    entitymanager.close();
	    return (messages.get(messages.size()-1).getMessageId()+1);
	}
	
	//Calculate the number of the rows and returns the result 
	public int generateListingID() {
        List<Listing> listings = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
       
		Query query = entitymanager.createQuery("Select l from Listing l ORDER BY l.id");
        listings =  query.getResultList();
		
        transaction.commit();
        entitymanager.close();
        return (listings.get(listings.size()-1).getId()+1);
	}

	//Calculate the number of the rows and returns the result 
	public int generateUserID() {
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        List users = entitymanager.createQuery("SELECT COUNT(*) FROM User").getResultList();
        entitymanager.close();
		return (Integer.parseInt(users.get(0).toString())+1);
	}
	
    public boolean usernameExists(String uname) {
    	boolean exists=false;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
		exists = (entitymanager.createNamedQuery("User.findByUsername").setParameter("username", uname).getResultList().size() == 1);
		tx.commit();
        entitymanager.close();
        return exists;
	}
    
    public void editUser(String username, String firstName, String lastName, String role, String email, String phone, String picture, String explanation) {	
		User user = getUserByUsername(username);
		
		if (user == null) return;

        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
		
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setRole(role);
		if (role.equals("Host") || role.equals("Host-Renter")) user.setActive((byte) 0);
		user.setEmail(email);
		user.setPhone(phone);
		user.setPicture(picture);
		user.setExplanation(explanation);
		
		entitymanager.getTransaction().commit();
        entitymanager.close();
	}
    
    public void activateUser(int userid) {
    	EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
		User user = entitymanager.find(User.class, userid);
		user.setActive((byte) 1);
		entitymanager.getTransaction().commit();
        entitymanager.close();
	}
	
	public void deactivateUser(int userid) {
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
		User user = entitymanager.find(User.class, userid);
		user.setActive((byte) 0);
		entitymanager.getTransaction().commit();
        entitymanager.close();
	}

	public List<Calendar> getCalendarList() throws ParseException {
		List<Calendar> calendarlist = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        
        //Query q = em.createQuery("Select u from User u");
        Query q = entitymanager.createNamedQuery("Calendar.findAll");
        calendarlist =  q.getResultList();
		
        tx.commit();
        entitymanager.close();
        return calendarlist;
	}

	public List<Calendar> getCalendarByListing(int listingid) throws ParseException {
		List<Calendar> calendarlist = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        
        //Query q = em.createQuery("Select u from User u");
		Query query = entitymanager.createQuery("Select c from Calendar c where c.listingId = :listingid");
		query.setParameter("listingid", listingid);
        calendarlist = query.getResultList();
		
        tx.commit();
        entitymanager.close();
        return calendarlist;
	}
	
	public void updatePassword(int userid, String password, String new_password)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
		User user = entitymanager.find(User.class, userid);
		
		if (user == null || !user.getPassword().equals(password)) {
	        entitymanager.close();
	        return;
		}
		
		user.setPassword(new_password);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editFirstname(int userid, String firstname)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

		User user = entitymanager.find(User.class, userid);
		
		if (user == null) {
	        entitymanager.close();
	        return;
		}
		
		user.setFirstName(firstname);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editLastname(int userid, String lastname)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

		User user = entitymanager.find(User.class, userid);
		
		if (user == null) {
	        entitymanager.close();
	        return;
		}
		
		user.setLastName(lastname);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editPhone(int userid, String phone)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

		User user = entitymanager.find(User.class, userid);
		
		if (user == null) {
	        entitymanager.close();
	        return;
		}
		
		user.setPhone(phone);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editEmail(int userid, String email)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

		User user = entitymanager.find(User.class, userid);
		
		if (user == null) {
	        entitymanager.close();
	        return;
		}
		
		user.setEmail(email);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editPicture(int userid, String picture)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

		User user = entitymanager.find(User.class, userid);
		
		if (user == null) {
	        entitymanager.close();
	        return;
		}
		
		user.setPicture(picture);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editListingPicture(int listingid, String picture)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

		Listing listing = this.getListing(listingid);
		
		if (listing == null) {
	        entitymanager.close();
	        return;
		}
		
		listing.setPictureUrl(picture);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editLocation(int userid, String location)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

		User user = entitymanager.find(User.class, userid);
		
		if (user == null) {
	        entitymanager.close();
	        return;
		}
		
		user.setLocation(location);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editExplanation(int userid, String explanation)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

		User user = entitymanager.find(User.class, userid);
		
		if (user == null) {
	        entitymanager.close();
	        return;
		}
		
		user.setExplanation(explanation);
		
		transaction.commit();
        entitymanager.close();
	}

	
	public List<Listing> search(String location, String checkin, String checkout, int guests, double pricemin, double pricemax) throws ParseException {
		List<Listing> listings = null;
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
		Query query = null;
		if (location.equals("") && checkin.equals(""))
		{
			System.out.println("Case 1");
			query = entitymanager.createQuery("Select l from Listing l where l.accommodates >= :guests and l.price >= :pricemin and l.price <= :pricemax ORDER BY l.price");
			query.setParameter("guests", guests);
			query.setParameter("pricemin", pricemin);
			query.setParameter("pricemax", pricemax);
		}
		else if (!location.equals("") && checkin.equals(""))
		{
			System.out.println("Case 2");
			query = entitymanager.createQuery("Select l from Listing l where l.city = :city and l.accommodates >= :guests and l.price >= :pricemin and l.price <= :pricemax ORDER BY l.price");
			query.setParameter("city", location);
			query.setParameter("guests", guests);
			query.setParameter("pricemin", pricemin);
			query.setParameter("pricemax", pricemax);
		}
		else if (location.equals("") && !checkin.equals("") && checkout.equals(""))
		{
			System.out.println("Case 3_1");			
			query = entitymanager.createQuery("Select l from Listing l,Calendar c where l.accommodates >= :guests and l.price >= :pricemin and l.price <= :pricemax "
					+ "and l.id = c.listingId and c.date = :checkin ORDER BY l.price");
			query.setParameter("checkin", checkin);
			query.setParameter("guests", guests);
			query.setParameter("pricemin", pricemin);
			query.setParameter("pricemax", pricemax);
		}
		else if (location.equals("") && !checkin.equals("") && !checkout.equals(""))
		{
			System.out.println("Case 3_2");
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-DD");
			Date checkinDate = (Date) formatter.parse(checkin);
			Date checkoutDate = (Date) formatter.parse(checkout);
            long diff = checkoutDate.getTime() - checkinDate.getTime();
            int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            
			query = entitymanager.createQuery("Select l from Listing l,Calendar c where l.accommodates >= :guests and l.price >= :pricemin and l.price <= :pricemax "
					+ "and l.minimumNights <= :days and l.maximumNights >= :days and l.id = c.listingId and c.date = :checkin ORDER BY l.price");
			query.setParameter("checkin", checkin);
			query.setParameter("guests", guests);
			query.setParameter("pricemin", pricemin);
			query.setParameter("pricemax", pricemax);
			query.setParameter("days", days);
		}
		else if (!location.equals("") && !checkin.equals("") && checkout.equals(""))
		{
			System.out.println("Case 4");         
			query = entitymanager.createQuery("Select l from Listing l,Calendar c where l.city = :city and l.accommodates >= :guests and l.price >= :pricemin and l.price <= :pricemax "
					+ "and l.id = c.listingId and c.date = :checkin ORDER BY l.price");
			query.setParameter("city", location);
			query.setParameter("checkin", checkin);
			query.setParameter("guests", guests);
			query.setParameter("pricemin", pricemin);
			query.setParameter("pricemax", pricemax);
		}
		else if (!location.equals("") && !checkin.equals("") && !checkout.equals(""))
		{
			System.out.println("Case 4");
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-DD");
			Date checkinDate = (Date) formatter.parse(checkin);
			Date checkoutDate = (Date) formatter.parse(checkout);
            long diff = checkoutDate.getTime() - checkinDate.getTime();
            int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            
			query = entitymanager.createQuery("Select l from Listing l,Calendar c where l.city = :city and l.accommodates >= :guests and l.price >= :pricemin and l.price <= :pricemax "
					+ "and l.minimumNights <= :days and l.maximumNights >= :days and l.id = c.listingId and c.date = :checkin ORDER BY l.price");
			query.setParameter("city", location);
			query.setParameter("checkin", checkin);
			query.setParameter("guests", guests);
			query.setParameter("pricemin", pricemin);
			query.setParameter("pricemax", pricemax);
			query.setParameter("days", days);
		}
		
        listings =  query.getResultList();
        System.out.println("Found "+listings.size()+" listings");
        
		entitymanager.getTransaction().commit();
        entitymanager.close();
        
        return listings;
	}
	
	public void bookCalendar(int calendarid, int userid) {
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Calendar calendar = entitymanager.find(Calendar.class, calendarid);
        
        System.out.println(calendar.getId()+" "+ userid);
        
		calendar.setUserid(userid);
		calendar.setAvailable("f");
		
		transaction.commit();
        entitymanager.close();
	}
    
    public Message getMessage(int messageId)
    {
    	Message message = null;
        
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        
        message = entitymanager.find(Message.class, messageId);
	
        tx.commit();
        entitymanager.close();
        
        return message;
    }
	
	public void postMessage(Message postmessage) {
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();
        
    	entitymanager.persist(postmessage);
    	entitymanager.flush();
    	
        tx.commit();
    	entitymanager.close();
	}
	
	public void deleteMessage(int messageId) {
		
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction tx = entitymanager.getTransaction();
        tx.begin();

    	Query query = entitymanager.createQuery("DELETE FROM Message m WHERE m.messageId = :id");
        int deletedCount = query.setParameter("id", messageId).executeUpdate();
        
        tx.commit();
    	entitymanager.close();
	}
	
	
	//Edit Listing
	public void editListingName(int listingid, String name)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setName(name);
		
		transaction.commit();
        entitymanager.close();
	}

	public void editListingPrice(int listingid, double price)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setPrice(price);
		
		transaction.commit();
        entitymanager.close();
	}

	public void editListingExtraPrice(int listingid, String extraprice)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setExtraPeople(extraprice);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editListingRoomType(int listingid, String roomType)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setRoomType(roomType);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editListingCity(int listingid, String city)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setCity(city);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editListingBeds(int listingid, int beds)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setBeds(beds);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editListingBathrooms(int listingid, double bathrooms)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setBathrooms(bathrooms);
		
		transaction.commit();
        entitymanager.close();
	}
		
	public void editListingBedrooms(int listingid, int bedrooms)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setBedrooms(bedrooms);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editListingSquareFeet(int listingid, String squareFeet)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setSquareFeet(squareFeet);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editListingDescription(int listingid, String description)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setDescription(description);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editListingMinimumNights(int listingid, int minimumNights)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setMinimumNights(minimumNights);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editListingCancellationPolicy(int listingid, String cancellationPolicy)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setCancellationPolicy(cancellationPolicy);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editListingRequiresLicense(int listingid, String requiresLicense)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setRequiresLicense(requiresLicense);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editListingAccommodates(int listingid, int accommodates)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setAccommodates(accommodates);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public void editListingAmenities(int listingid, String amenities)
	{
		EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();

        Listing listing = entitymanager.find(Listing.class, listingid);
        listing.setAmenities(amenities);
		
		transaction.commit();
        entitymanager.close();
	}
	
	public List<Listing> getHostListings(int hostid){
        List<Listing> listings = null;
        EntityManager entitymanager = JPAResource.factory.createEntityManager();
        EntityTransaction transaction = entitymanager.getTransaction();
        transaction.begin();
        
        Query query = entitymanager.createQuery("Select l from Listing l where l.hostId = :hostid");
        query.setParameter("hostid", hostid);
        listings =  query.getResultList();
		
        transaction.commit();
        entitymanager.close();
        
        return listings;
	}
	
}
