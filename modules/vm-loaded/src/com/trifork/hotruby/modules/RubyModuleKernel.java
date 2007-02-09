package com.trifork.hotruby.modules;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.trifork.hotruby.callable.PublicMethod;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod2;
import com.trifork.hotruby.classes.RubyClassModule;
import com.trifork.hotruby.objects.IRubyClass;
import com.trifork.hotruby.objects.IRubyInteger;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubyString;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyException;
import com.trifork.hotruby.objects.RubyInteger;
import com.trifork.hotruby.objects.RubyModule;
import com.trifork.hotruby.objects.RubyProc;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.runtime.EvalContext;
import com.trifork.hotruby.runtime.EvalMissingEnvironException;
import com.trifork.hotruby.runtime.Global;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.PublicMethodN;
import com.trifork.hotruby.runtime.RaisedException;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public final class RubyModuleKernel extends RubyModule {
	static public RubyModuleKernel instance;

	static Selector TO_S;

	public void init(MetaModule meta) {
		instance = (RubyModuleKernel) this;
		super.init(meta);

		TO_S = meta.getRuntime().getSelector(meta, "to_s");

		meta.register_instance_method("at_exit", new PublicMethod0() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				RubyProc proc = new RubyProc(block);
				getRuntime().register_at_exit(proc);
				return proc;
			}
			
		});
		
		meta.register_instance_method("public", new PublicMethodN() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				// TODO: implement
				return LoadedRubyRuntime.NIL;
			}});
		
		meta.register_instance_method("private", new PublicMethodN() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				// todo: implement
				return LoadedRubyRuntime.NIL;
			}});
		
		meta.register_module_method("eval_file", new PublicMethod2() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
					IRubyObject arg2, RubyBlock block) {
				IRubyString file = RubyString.induce_from(arg1);
				LoadedRubyRuntime.instance.load(file.asSymbol(), arg2);
				return LoadedRubyRuntime.NIL;
			}
		});

		meta.register_instance_method("caller", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
					RubyBlock block) {
				if (args.length == 0) {
					return getRuntime().caller(1);
				} else {
					int i = RubyInteger.induced_from(args[0]).intValue();
					return getRuntime().caller(i);
				}
			}
		});

		meta.register_instance_method("command", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
					RubyBlock block) {
				String[] cmd_args = new String[args.length + 2];
				cmd_args[0] = "/bin/sh";
				cmd_args[1] = "-c";
				for (int i = 0; i < args.length; i++) {
					cmd_args[i + 2] = ((IRubyString) args[i].fast_to_s(TO_S))
							.asSymbol();
				}

				Process p;

				try {
					p = Runtime.getRuntime().exec(cmd_args, null, null);

				} catch (IOException e) {
					throw new RuntimeException("exec failed");
				}

				try {
					p.getOutputStream().close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				InputStream si = p.getInputStream();
				StringBuilder sb = new StringBuilder();
				grab(si, sb);
				try {
					si.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				/*
				 * InputStream si2 = p.getErrorStream(); StringBuilder sb2 = new
				 * StringBuilder(); grab (si2, sb2); try { si2.close(); } catch
				 * (IOException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 * 
				 * System.out.println(sb2);
				 * 
				 * System.out.println(p.exitValue());
				 */

				String value = sb.toString();
				return getRuntime().newString(value);
			}

			private void grab(InputStream i, StringBuilder sb) {
				BufferedReader br = new BufferedReader(new InputStreamReader(i));

				String line;
				try {
					boolean first = true;
					while ((line = br.readLine()) != null) {
						if (!first) {
							sb.append('\n');
						}
						sb.append(line);
						first = false;
					}
				} catch (EOFException ex) {
					// ignore //
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		meta.register_instance_method("block_given?", new PublicMethod0() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				throw new EvalMissingEnvironException();
			}
			
			@Override
			public IRubyObject call_eval(IRubyObject receiver, RubyBlock block, EvalContext ctx) {
				return bool(ctx.get_block() != null);
			}
			
		});

		meta.register_instance_method("eval", new RubyMethod() {

			@Override
			public IRubyObject call_eval(IRubyObject receiver,
					IRubyObject arg1, RubyBlock block, EvalContext ctx) {
				System.out.println("call_eval");
				String text = ((IRubyString) arg1).asSymbol();
				return LoadedRubyRuntime.instance.eval(text, ctx, "script", 0);
			}

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				throw wrongArgs(receiver, 0);
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg,
					RubyBlock block) {
				throw new EvalMissingEnvironException();
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
					IRubyObject arg2, RubyBlock block) {
				String text = ((IRubyString) arg1).asSymbol();
				EvalContext ctx = arg2 == LoadedRubyRuntime.NIL ? null
						: (EvalContext) arg2;

				return LoadedRubyRuntime.instance.eval(text, ctx, "script", 0);
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
					RubyBlock block) {
				if (args.length == 1) {
					throw new EvalMissingEnvironException();
				}

				if (args.length == 0) {
					throw wrongArgs(receiver, 0);
				}

				String text = ((IRubyString) args[0]).asSymbol();
				EvalContext ctx = (EvalContext) args[1];
				String file = args.length > 2 ? ((IRubyString) args[2])
						.asSymbol() : "script";
				int line = args.length > 3 ? ((IRubyInteger) args[2])
						.intValue() : 0;

				return LoadedRubyRuntime.instance.eval(text, ctx, file, line);
			}

			@Override
			public int getArity() {
				// requires at least one argument
				return -2;
			}

		});

		final Global curr_exception = getRuntime().getGlobal("$!");
		final Global curr_trace = getRuntime().getGlobal("$@");
		final Selector selector_to_str = getRuntime().getSelector(meta, "to_str");
		final Selector selector_new = getRuntime().getSelector(meta, "new");
		final Selector selector_set_backtrace = getRuntime().getSelector(meta, "set_backtrace");
		
		meta.register_instance_method("raise", new PublicMethod() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				// re-raise current exception
				throw new RaisedException(curr_exception.get());
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				if (arg instanceof RubyString) {
					throw getRuntime().newRuntimeError((IRubyString)arg.fast_to_str(selector_to_str));
				} 

				curr_exception.set(arg);
				throw new RaisedException(arg);
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1, IRubyObject arg2, RubyBlock block) {
				return call(receiver, arg1, arg2, getRuntime().caller(1), block);
			}

			public IRubyObject call(IRubyObject receiver, IRubyObject arg1, IRubyObject arg2, IRubyObject arg3, RubyBlock block) {
				
				if (!(arg1 instanceof IRubyClass)) {
					throw getRuntime().newArgumentError("argument to `raise' is not a class");
				}
				
				IRubyObject exception = arg1.do_select(selector_new).call(receiver, arg2, (RubyBlock)null);
				
				exception.do_select(selector_set_backtrace).call(receiver, arg3, (RubyBlock)null);
				
				curr_exception.set(exception);
				throw new RaisedException(exception);
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				switch (args.length) {
				case 0: return call(receiver, block);
				case 1: return call(receiver, args[0], block);
				case 2: return call(receiver, args[0], args[1], block);
				case 3: return call(receiver, args[0], args[1], args[2], block);
				default: throw wrongArgs(receiver, args.length);
				}
			}

			@Override
			public int getArity() {
				return -2;
			}
			
		});
	}

	public interface SelectKernelMethod {
		RubyMethod get_methodForKernel();
	}

	public RubyMethod select(Selector sel) {
		if (sel instanceof SelectKernelMethod) {
			return ((SelectKernelMethod) sel).get_methodForKernel();
		} else {
			return LoadedRubyRuntime.resolve_method((RubyModuleKernel) this,
					sel, SelectKernelMethod.class);
		}
	}

	public RubyClass get_class() {
		return RubyClassModule.instance;
	}

}
