package suneesh.springbootstarter.topic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

	@Autowired
	private TopicRepository topicRepository;
	
	public List<Topic> getAllTopics() {
		List<Topic> topics = new ArrayList<>();
		topicRepository.findAll().forEach(topics::add);
		return topics;
	}

	public void addTopic(Topic topic) {
		topicRepository.save(topic);
	}

	public Topic getTopic(String id) {
		return topicRepository.findOne(id);
	}

	public void deleteTopic(String id) {
		topicRepository.delete(id);
	}

	public void updateTopic(Topic topic, String id) {
		topicRepository.save(topic);
	}
	
	
}
