package com.courses.controller;

import com.courses.dto.ResourceDto;
import com.courses.entity.Resource;
import com.courses.mapper.impl.ResourceMapper;
import com.courses.service.IResourceService;
import com.courses.service.impl.ResourceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Validated
@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private IResourceService resourceService;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/{id}")
    public ResourceDto getById(@PathVariable("id") @Min(0L) @Max(Long.MAX_VALUE) Long id) {
        return resourceMapper.entityToDto(resourceService.findById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ObjectNode saveAudioResource(@RequestPart MultipartFile file) {
        Resource resource = resourceService.createResource();
        try (InputStream is = file.getInputStream()) {
            byte[] bytes = is.readAllBytes();
            resource.setAudioBytes(bytes);
            resource = resourceService.saveResource(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObjectNode node = objectMapper.createObjectNode();
        node.put("id", resource.getId());
        return node;
    }

    @DeleteMapping
    public ObjectNode deleteResource(@RequestParam("ids") @Length(max = 200, message = "Parameter length mustn't be more then 200 characters")
                                     @Pattern(regexp = "^(\\d{1,10}|(\\d{1,10},(\\d{1,10},){0,20}\\d{1,10}))$", message = "Value must be comma-separated ten-digit numbers") String idsString) {
        List<Long> ids = Arrays.stream(idsString.split(",")).map(Long::parseLong).toList();
        ids.forEach(resourceService::deleteById);
        ArrayNode arrayNode = objectMapper.createObjectNode()
                .putArray("ids");
        ids.forEach(arrayNode::add);
        ObjectNode node = objectMapper.createObjectNode();
        node.set("ids", arrayNode);
        return node;
    }
}
