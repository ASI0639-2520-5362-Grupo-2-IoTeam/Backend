package pe.iotteam.plantcare.community.domain.model.commands;

import pe.iotteam.plantcare.community.domain.model.valueobjects.UserId;

public record CreatePostCommand(UserId authorId, String title, String content) {}