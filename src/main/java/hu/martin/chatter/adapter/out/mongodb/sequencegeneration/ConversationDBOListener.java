package hu.martin.chatter.adapter.out.mongodb.sequencegeneration;

import hu.martin.chatter.adapter.out.mongodb.ConversationDBO;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class ConversationDBOListener extends AbstractMongoEventListener<ConversationDBO> {

    private final SequenceGeneratorService sequenceGeneratorService;

    public ConversationDBOListener(SequenceGeneratorService sequenceGeneratorService) {
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<ConversationDBO> event) {
        ConversationDBO source = event.getSource();
        if (source.getId() == null) {
            Long nextId = sequenceGeneratorService.generateSequence(ConversationDBO.SEQUENCE_NAME);
            source.setId(nextId);
        }
    }
}
