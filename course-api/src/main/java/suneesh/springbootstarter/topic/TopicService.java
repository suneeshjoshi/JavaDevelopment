package suneesh.springbootstarter.topic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TopicService {

	private List<Topic> topics= new ArrayList<>(Arrays.asList(		
			new Topic("1","Suneesh","User 1"),
			new Topic("2","Charitha","User 2"),
			new Topic("3","Aadya","User 3")
			));

	public List<Topic> getAllTopics() {
		return topics;
	}

	public Topic getTopic(String id) {
		return topics.stream().filter(f->f.getId().equals(id)).findFirst().get();
	}

	public void addTopic(Topic topic) {
		topics.add(topic);
	}

	public void deleteTopic(String id) {
		topics.removeIf(f->f.getId().equalsIgnoreCase(id));
	}

	public void updateTopic(Topic topic, String id) {
		for(int i=0; i< topics.size(); i++) {
			Topic t = topics.get(i);
			if(t.getId().equals(id)) {
				topics.set(i, topic);
			}
		}
	}
	
	
}
