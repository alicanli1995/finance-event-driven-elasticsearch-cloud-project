CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('1273a5e2-15b5-4a06-968c-01513aa13be5', 'app_user', 'Standard', 'User');
INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('7c60fd48-89be-40c6-8b96-3198b62fd986', 'app_admin', 'Admin', 'User');
INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('8034bb2f-6558-4832-9ddf-bafe0168ac24', 'app_super_user', 'Super', 'User');


insert into documents(id, document_id)
values ('c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'd2231a41-f22f-4bf6-a2b3-5f6a9a573ce7');
insert into documents(id, document_id)
values ('f2b2d644-3a08-4acb-ae07-20569f6f2a01', '425ae1dc-45af-4ed0-89bb-bf22d94e47dd');
insert into documents(id, document_id)
values ('90573d2b-9a5d-409e-bbb6-b94189709a19', '494508cc-0047-4d62-a13e-346b983c3732');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'7c60fd48-89be-40c6-8b96-3198b62fd986', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'7c60fd48-89be-40c6-8b96-3198b62fd986', 'f2b2d644-3a08-4acb-ae07-20569f6f2a01', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'7c60fd48-89be-40c6-8b96-3198b62fd986', '90573d2b-9a5d-409e-bbb6-b94189709a19', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), '8034bb2f-6558-4832-9ddf-bafe0168ac24', '90573d2b-9a5d-409e-bbb6-b94189709a19', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), '1273a5e2-15b5-4a06-968c-01513aa13be5', '90573d2b-9a5d-409e-bbb6-b94189709a19', 'READ');


