package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.example.demo.NaverMovie;

import mapper.CinemaMapper;
import mapper.MovieMapper;

@Controller
public class MovieController {
	@Autowired
	private NaverMovie naverMovie;
	
	@Resource(name="mapper.MovieMapper")
	MovieMapper movieMapper;
	
	@Resource(name="mapper.CinemaMapper")
	CinemaMapper cinemaMapper;
	
	
	@RequestMapping("/")
	public ModelAndView home() throws Exception {
		ModelAndView mav = new ModelAndView();
		
		List<Movie> list = movieMapper.MovieList();
		mav.addObject("movieList", list);
		mav.setViewName("home");
		return mav;
	}
	
	//search 
	@RequestMapping("/search")
	public ModelAndView movieList(@RequestParam(required=false)String text) {
		ModelAndView mav = new ModelAndView();
		
		if(text != null) {
			mav.addObject("movieList", naverMovie.searchMovie(text, 10));
		}
		mav.setViewName("/movieList");
		
		return mav;
	}
	
	@RequestMapping("/insert_movie")
	public String movieInsert(HttpServletRequest request) throws Exception {
		Movie movie = new Movie();
		movie.setM_title(request.getParameter("title"));
		movie.setM_director(request.getParameter("director"));
		movie.setM_img(request.getParameter("img"));
		movie.setM_pubDate(request.getParameter("pubDate"));
		movie.setM_rating(request.getParameter("rating"));
		
		movieMapper.MovieInsert(movie);
		return "redirect:/";
	}
	
	@RequestMapping("/addSchedule")
	public ModelAndView addSchedule() throws Exception {
		List<Movie> m_list = movieMapper.MovieList();
		List<Cinema> c_list = cinemaMapper.CinemaList();
		
		
		ModelAndView schedule = new ModelAndView();
		schedule.addObject("movieList", m_list);
		schedule.addObject("cinemaList", c_list);
		schedule.setViewName("schedule");
		
		return schedule;
	}
	
	@RequestMapping("/insert_schedule")
	public String insertSchedule(HttpServletRequest request) throws Exception {
		Schedule schedule = new Schedule();
		
		System.out.println(Integer.parseInt(request.getParameter("cinema")));
		System.out.println(request.getParameter("date"));
		System.out.println(request.getParameter("s_time"));
		System.out.println(request.getParameter("e_time"));
		
		
		schedule.setM_no(Integer.parseInt(request.getParameter("movie")));
		schedule.setC_no(Integer.parseInt(request.getParameter("cinema")));
		schedule.setDate(request.getParameter("date"));
		schedule.setS_time(request.getParameter("s_time"));
		schedule.setE_time(request.getParameter("e_time"));
		
		movieMapper.ScheduleInsert(schedule);
		return "redirect:/home";
	}
	
	@RequestMapping("/login")
	public ModelAndView getUser(HttpServletRequest request) throws Exception {
		int m_no = Integer.parseInt(request.getParameter("m_no"));
		ModelAndView mav = new ModelAndView();
		Movie movie = new Movie();
		movie.setM_no(m_no);
		
		mav.addObject("movie", movie);
		
		mav.setViewName("user");
		return mav;
	}
	
	@RequestMapping("/booking")
	public ModelAndView booking(HttpServletRequest request) throws Exception {
		int m_no = Integer.parseInt(request.getParameter("m_no"));
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		List<Schedule> schedule = movieMapper.scheduleList(m_no);
		List<String> cinema = new ArrayList<String>();
		
		for(int i = 0; i<schedule.size(); i++) {
			cinema.add(cinemaMapper.CinemaName(schedule.get(i).getC_no()));
		}
		
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("m_no", m_no);
		mav.addObject("id", id);
		mav.addObject("pw", pw);
		
		mav.addObject("cinema", cinema);
		mav.addObject("scheduleList", schedule);
		
		mav.setViewName("booking");
		
		return mav;
	}
	@RequestMapping("/booking_action")
	public String insert_booking(HttpServletRequest request) throws Exception{
		int m_no = Integer.parseInt(request.getParameter("m_no"));
		int c_no = Integer.parseInt(request.getParameter("c_no"));
		
		String b_no = ""+System.currentTimeMillis();
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String date = request.getParameter("date");
		String s_time = request.getParameter("s_time");
		String e_time = request.getParameter("e_time");
		
		Booking b = new Booking();
		b.setB_no(b_no);b.setM_no(m_no);
		b.setC_no(c_no);b.setDate(date);
		b.setId(id);b.setPw(pw);
		b.setS_time(s_time);b.setE_time(e_time);
		
		movieMapper.Booking(b);
		return "home";
		
	}
	
	@RequestMapping("/book_user")
	public String book_user() throws Exception {
		return "/book_user";
	}
	
	@RequestMapping("/booking_list")
	public ModelAndView booking_list(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		User u = new User();
		
		u.setId(id);u.setPw(pw);
		List<Booking> b = movieMapper.BookList(u);
		
		System.out.println(b.get(0).getB_no());
		
		List<String> m = new ArrayList<String>();
		List<String> c = new ArrayList<String>();
		
		
		for(int i = 0; i<b.size(); i++) {
			c.add(cinemaMapper.CinemaName(b.get(i).getC_no()));
			m.add(movieMapper.MovieName(b.get(i).getM_no()));
		}
		
		mav.addObject("book", b);
		mav.addObject("movie", m);
		mav.addObject("cinema", c);
		
		mav.setViewName("book_detail");
		
		return mav;
	}
	
	@RequestMapping("/cinema")
	public ModelAndView cinemaList() throws Exception {
		List<Cinema> list = cinemaMapper.CinemaList();
		ModelAndView cinemaList = new ModelAndView();
		cinemaList.addObject("cinemaList", list);
		
		cinemaList.setViewName("/insert_detail");
		return cinemaList;
	}
	
}
