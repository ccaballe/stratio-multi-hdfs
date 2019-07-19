truncate table public.dyplon_user CASCADE;
truncate table public.dyplon_group CASCADE;
truncate table public.dyplon_tenant CASCADE;
truncate table public.dyplon_user_tenant CASCADE;
truncate table public.dyplon_group_tenant CASCADE;
truncate table public.dyplon_user_group CASCADE;
truncate table public.dyplon_gru_gru CASCADE;
truncate table public.dyplon_group_tenant CASCADE;
truncate table public.dyplon_resource CASCADE;
truncate table public.dyplon_resource_type CASCADE;
truncate table public.dyplon_service_type CASCADE;

INSERT INTO public.dyplon_tenant(id, name) VALUES ('dyplon_system_tenant', 'dyplon_system_tenant');


