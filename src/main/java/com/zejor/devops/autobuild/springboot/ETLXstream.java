package com.zejor.devops.autobuild.springboot;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class ETLXstream extends XStream {
    /*
     * (non-Javadoc)
     *
     * @see
     * com.thoughtworks.xstream.XStream#wrapMapper
     */
    @Override
    protected MapperWrapper wrapMapper(MapperWrapper next) {
        return new MapperWrapper(next) {
            /*
             * (non-Javadoc)
             *
             * @see
             * com.thoughtworks.xstream.mapper.MapperWrapper#shouldSerializeMember
             */
            @Override
            public boolean shouldSerializeMember(@SuppressWarnings("rawtypes") Class definedIn, String fieldName) {
                // 不能识别的节点，掠过。
                if (definedIn == Object.class) {
                    return false;
                }
                // 节点名称为fileName的掠过
                if (fieldName.equals("fileName")) {
                    return false;
                }
                return super.shouldSerializeMember(definedIn, fieldName);
            }
        };
    }
}