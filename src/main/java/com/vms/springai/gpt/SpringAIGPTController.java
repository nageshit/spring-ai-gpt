package com.vms.springai.gpt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/springai")
public class SpringAIGPTController {

    private ChatClient chatClient;

    public SpringAIGPTController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    @GetMapping("/home")
    public String home(){
        return  "Hello Spring AI GPT home";
    }

    @GetMapping("/")
    public List<String> findPopularPlayersBySports(@RequestParam
                                     String sports) {

        ListOutputConverter converter
                = new ListOutputConverter(new DefaultConversionService());

        String message = """
                List of 5 most popular personalities in {sports}.
                {format}
                """;


        PromptTemplate template
                = new PromptTemplate(message);

        Prompt prompt = template
                .create(Map.of("sports",sports, "format", converter.getFormat()));

        ChatResponse response = chatClient
                .prompt(prompt)
                .call()
                .chatResponse();

        return converter.convert(response.getResult().getOutput().getContent());

    }


    @GetMapping("/test")
    public Player findPopularPlayers(@RequestParam String sports) {

        BeanOutputConverter<Player> converter
                = new BeanOutputConverter<>(Player.class);

        String message = """
                Generate a list of Career achievements for the sportsperson {sports}.\s
                Include the Player as the key and achievements as the value for it
                {format}
                """;


        PromptTemplate template
                = new PromptTemplate(message);

        Prompt prompt = template
                .create(Map.of("sports",sports, "format", converter.getFormat()));

        ChatResponse response = chatClient
                .prompt(prompt)
                .call()
                .chatResponse();

        return converter.convert(response.getResult().getOutput().getContent());

    }

}
