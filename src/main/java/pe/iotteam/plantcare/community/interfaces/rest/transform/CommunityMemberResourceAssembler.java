package pe.iotteam.plantcare.community.interfaces.rest.transform;

import pe.iotteam.plantcare.community.domain.model.aggregates.CommunityMember;
import pe.iotteam.plantcare.community.interfaces.rest.resources.CommunityMemberResource;

public class CommunityMemberResourceAssembler {

    public static CommunityMemberResource toResource(CommunityMember member) {
        return new CommunityMemberResource(
                member.getId(),
                member.getRole().name(),
                member.getJoinedAt()
        );
    }
}