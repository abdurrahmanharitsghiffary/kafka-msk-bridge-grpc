from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class ProduceRequest(_message.Message):
    __slots__ = ("topic", "key", "value")
    TOPIC_FIELD_NUMBER: _ClassVar[int]
    KEY_FIELD_NUMBER: _ClassVar[int]
    VALUE_FIELD_NUMBER: _ClassVar[int]
    topic: str
    key: str
    value: str
    def __init__(self, topic: _Optional[str] = ..., key: _Optional[str] = ..., value: _Optional[str] = ...) -> None: ...

class ProduceResponse(_message.Message):
    __slots__ = ("success", "message")
    SUCCESS_FIELD_NUMBER: _ClassVar[int]
    MESSAGE_FIELD_NUMBER: _ClassVar[int]
    success: bool
    message: str
    def __init__(self, success: bool = ..., message: _Optional[str] = ...) -> None: ...

class ConsumeRequest(_message.Message):
    __slots__ = ("topic", "partition", "consumer_group")
    TOPIC_FIELD_NUMBER: _ClassVar[int]
    PARTITION_FIELD_NUMBER: _ClassVar[int]
    CONSUMER_GROUP_FIELD_NUMBER: _ClassVar[int]
    topic: str
    partition: int
    consumer_group: str
    def __init__(self, topic: _Optional[str] = ..., partition: _Optional[int] = ..., consumer_group: _Optional[str] = ...) -> None: ...

class ConsumeResponse(_message.Message):
    __slots__ = ("key", "value", "offset")
    KEY_FIELD_NUMBER: _ClassVar[int]
    VALUE_FIELD_NUMBER: _ClassVar[int]
    OFFSET_FIELD_NUMBER: _ClassVar[int]
    key: str
    value: str
    offset: int
    def __init__(self, key: _Optional[str] = ..., value: _Optional[str] = ..., offset: _Optional[int] = ...) -> None: ...
