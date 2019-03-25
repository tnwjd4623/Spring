package mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.Movie;

@Repository("mapper.MovieMapper")
public interface MovieMapper {
	//Movie count
	public int MovieCount() throws Exception;
	
	//Insert Movie
	public void MovieInsert(Movie movie) throws Exception;
	
	//Movie list
	public List<Movie> MovieList() throws Exception;
	
	//Delete Movie
	public void MovieDelete() throws Exception;
}
