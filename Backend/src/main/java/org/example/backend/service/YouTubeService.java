package org.example.backend.service;

import org.example.backend.model.VideoRec;
import org.example.backend.repository.VideoRecRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class YouTubeService {

    private final String API_KEY = "AIzaSyCa_uYhimzjUEIUcurxkGj8Z0m_jVsQX6c";
    private final String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3/search";
    private final VideoRecRepository videoRecRepository;

    public YouTubeService(VideoRecRepository videoRecRepository) {
        this.videoRecRepository = videoRecRepository;
    }

    public List<String> searchVideos(String query, int maxResults) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(YOUTUBE_API_URL)
                .queryParam("part", "snippet")
                .queryParam("q", query)
                .queryParam("type", "video")
                .queryParam("maxResults", maxResults)
                .queryParam("key", API_KEY)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

        List<String> videoLinks = new ArrayList<>();

        if (items != null) {
            for (Map<String, Object> item : items) {
                Map<String, Object> id = (Map<String, Object>) item.get("id");
                if ("youtube#video".equals(id.get("kind"))) {
                    String videoId = (String) id.get("videoId");
                    videoLinks.add("https://www.youtube.com/watch?v=" + videoId);
                }
            }
        }

        return videoLinks;
    }

    public List<VideoRec> saveVideosForStress(String query, int maxResults) {
        List<String> videoLinks = searchVideos(query, maxResults);
        List<VideoRec> savedVideos = new ArrayList<>();

        for (String link : videoLinks) {
            VideoRec video = new VideoRec();
            video.setTitle("Relaxation Video");
            video.setVideoLink(link);
            video.setDescription("Vidéo générée dynamiquement pour relaxation.");
            savedVideos.add(videoRecRepository.save(video));
        }

        return savedVideos;
    }
}