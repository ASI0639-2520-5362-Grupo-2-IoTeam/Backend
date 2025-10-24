package pe.iotteam.plantcare.community.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.iotteam.plantcare.community.domain.model.aggregates.CommunityMember;
import pe.iotteam.plantcare.community.domain.model.entities.Post;
import pe.iotteam.plantcare.community.infrastructure.persistence.jpa.repositories.CommunityMemberRepository;
import pe.iotteam.plantcare.community.infrastructure.persistence.jpa.repositories.PostRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CommunityProfileQueryService {

    private final CommunityMemberRepository memberRepository;
    private final PostRepository postRepository;

    public CommunityProfileQueryService(CommunityMemberRepository memberRepository,
                                        PostRepository postRepository) {
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    public CommunityMember getMemberProfile(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Miembro no encontrado"));
    }

    public List<Post> getPostsByMember(UUID memberId) {
        CommunityMember member = getMemberProfile(memberId);
        return postRepository.findByAuthor(member);
    }
}
