package jdroplet.data.model;

import jdroplet.annotation.db.DataField;
import jdroplet.annotation.db.DataFieldType;
import jdroplet.annotation.db.DataTable;

/**
 * Created by kuibo on 2017/11/8.
 */
@DataTable(name="jdroplet_section_objects")
public class SectionRelationship {

    @DataField(name="object_id", length = 10, type = DataFieldType.Integer)
    private int objectId;

    @DataField(name="section_id", length = 10, type = DataFieldType.Integer)
    private int sectionId;
}
